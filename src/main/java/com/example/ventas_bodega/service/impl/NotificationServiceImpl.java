package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.NotificationDto;
import com.example.ventas_bodega.entity.CajaEntity;
import com.example.ventas_bodega.entity.NotificationEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.exceptions.NotFoundException;
import com.example.ventas_bodega.mapper.NotificationMapper;
import com.example.ventas_bodega.repository.CajaRepository;
import com.example.ventas_bodega.repository.NotificationRepository;
import com.example.ventas_bodega.repository.ProductRepository;
import com.example.ventas_bodega.repository.ReceivableRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final int RECEIVABLE_DUE_SOON_DAYS = 3;
    private static final int CAJA_OPEN_HOURS_THRESHOLD = 16;

    private final NotificationRepository notificationRepository;
    private final ReceivableRepository receivableRepository;
    private final ProductRepository productRepository;
    private final CajaRepository cajaRepository;

    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            ReceivableRepository receivableRepository,
            ProductRepository productRepository,
            CajaRepository cajaRepository) {
        this.notificationRepository = notificationRepository;
        this.receivableRepository = receivableRepository;
        this.productRepository = productRepository;
        this.cajaRepository = cajaRepository;
    }

    @Override
    public void checkReceivablesDueSoon() {
        List<Object[]> rows = receivableRepository.findReceivablesDueSoon(RECEIVABLE_DUE_SOON_DAYS);
        for (Object[] row : rows) {
            Long companyId = toLong(row[0]);
            Long receivableId = toLong(row[1]);
            String clientName = row[2] == null ? "Un cliente" : row[2].toString();
            String balance = toMoney(row[3]);
            String message = "La cuenta de " + clientName + " por S/ " + balance + " vence en los próximos " + RECEIVABLE_DUE_SOON_DAYS + " días.";
            createIfNotExists(companyId, "RECEIVABLE_DUE_SOON", "warning", "RECEIVABLE", receivableId, message);
        }
    }

    @Override
    public void checkReceivablesOverdue() {
        List<Object[]> rows = receivableRepository.findReceivablesOverdue();
        for (Object[] row : rows) {
            Long companyId = toLong(row[0]);
            Long receivableId = toLong(row[1]);
            String clientName = row[2] == null ? "Un cliente" : row[2].toString();
            String balance = toMoney(row[3]);
            String message = "La cuenta de " + clientName + " por S/ " + balance + " está vencida.";
            createIfNotExists(companyId, "RECEIVABLE_OVERDUE", "critical", "RECEIVABLE", receivableId, message);
        }
    }

    @Override
    public void checkLowStock() {
        List<Object[]> rows = productRepository.findLowStockProducts();
        for (Object[] row : rows) {
            Long companyId = toLong(row[0]);
            Long productId = toLong(row[1]);
            String name = row[2] == null ? "Un producto" : row[2].toString();
            String stock = toPlainNumber(row[3]);
            String message = "El producto \"" + name + "\" tiene poco stock (" + stock + ").";
            createIfNotExists(companyId, "STOCK_LOW", "warning", "PRODUCT", productId, message);
        }
    }

    @Override
    public void checkOutOfStock() {
        List<Object[]> rows = productRepository.findOutOfStockProducts();
        for (Object[] row : rows) {
            Long companyId = toLong(row[0]);
            Long productId = toLong(row[1]);
            String name = row[2] == null ? "Un producto" : row[2].toString();
            String message = "El producto \"" + name + "\" se quedó sin stock.";
            createIfNotExists(companyId, "STOCK_OUT", "critical", "PRODUCT", productId, message);
        }
    }

    @Override
    public void checkOpenCajaTooLong() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(CAJA_OPEN_HOURS_THRESHOLD);
        List<CajaEntity> openCajas = cajaRepository.findOpenCajasOlderThan(threshold);
        for (CajaEntity caja : openCajas) {
            if (caja.getUser() == null || caja.getUser().getCompany() == null) {
                continue;
            }
            Long companyId = caja.getUser().getCompany().getCompanyId();
            String message = "Hay una caja abierta hace más de " + CAJA_OPEN_HOURS_THRESHOLD + " horas.";
            createIfNotExists(companyId, "CAJA_OPEN_TOO_LONG", "warning", "CAJA", caja.getId(), message);
        }
    }

    @Override
    public Page<NotificationDto> getNotifications(UserEntity user, Boolean unreadOnly, int page, int size) {
        Long companyId = user.getCompany().getCompanyId();
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationEntity> notifications = Boolean.TRUE.equals(unreadOnly)
                ? notificationRepository.findByCompanyIdAndReadOrderByCreatedAtDesc(companyId, false, pageable)
                : notificationRepository.findByCompanyIdOrderByCreatedAtDesc(companyId, pageable);
        return notifications.map(NotificationMapper::entityToDto);
    }

    @Override
    public long countUnread(UserEntity user) {
        return notificationRepository.countByCompanyIdAndReadFalse(user.getCompany().getCompanyId());
    }

    @Override
    public MessageResponse markAsRead(Long notificationId, UserEntity user) {
        NotificationEntity notification = notificationRepository
                .findByNotificationIdAndCompanyId(notificationId, user.getCompany().getCompanyId())
                .orElseThrow(() -> new NotFoundException("La notificación no existe"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return new MessageResponse("Notificación marcada como leída", true);
    }

    @Override
    @Transactional
    public MessageResponse markAllAsRead(UserEntity user) {
        notificationRepository.markAllAsReadByCompanyId(user.getCompany().getCompanyId());
        return new MessageResponse("Notificaciones marcadas como leídas", true);
    }

    // No renotifica mientras la anterior siga sin leerse: evita spamear la misma cuenta/producto/caja en cada corrida del cron.
    private void createIfNotExists(Long companyId, String type, String severity, String referenceType, Long referenceId, String message) {
        if (companyId == null || referenceId == null) {
            return;
        }
        if (notificationRepository.existsByCompanyIdAndTypeAndReferenceIdAndReadFalse(companyId, type, referenceId)) {
            return;
        }
        NotificationEntity entity = new NotificationEntity();
        entity.setCompanyId(companyId);
        entity.setType(type);
        entity.setSeverity(severity);
        entity.setReferenceType(referenceType);
        entity.setReferenceId(referenceId);
        entity.setMessage(message);
        entity.setRead(false);
        notificationRepository.save(entity);
    }

    private Long toLong(Object value) {
        return value == null ? null : ((Number) value).longValue();
    }

    private String toMoney(Object value) {
        BigDecimal amount = value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String toPlainNumber(Object value) {
        BigDecimal amount = value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
        return amount.stripTrailingZeros().toPlainString();
    }
}
