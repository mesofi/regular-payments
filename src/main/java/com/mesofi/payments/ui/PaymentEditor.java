package com.mesofi.payments.ui;

import com.mesofi.payments.entity.Payment;
import com.mesofi.payments.entity.Unit;
import com.mesofi.payments.repository.PaymentRepository;
import com.mesofi.payments.repository.UnitRepository;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

@UIScope
@SpringComponent
public class PaymentEditor extends VerticalLayout implements KeyNotifier {

    @Serial
    private static final long serialVersionUID = -3783441277152047445L;

    /**
     * The currently edited customer
     */
    private Payment payment;

    private final PaymentRepository paymentRepository;
    private final UnitRepository unitRepository;

    /* Fields to edit properties in Customer entity */
    TextField paymentAmount = new TextField("Payment Amount");
    TextField paymentDate = new TextField("Payment Date");

    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, save, delete);

    Binder<Payment> binder = new Binder<>(Payment.class);

    @Autowired
    public PaymentEditor(PaymentRepository paymentRepository, UnitRepository unitRepository) {
        this.paymentRepository = paymentRepository;
        this.unitRepository = unitRepository;
        add(paymentAmount, paymentDate, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        save.addClickListener(e -> save());
    }

    private void save() {
        Payment payment1 = new Payment();
        payment1.setPaymentDate("dddd");
        payment1.setAmount(new BigDecimal("4.5"));

        //for( unitRepository.findAll(): Unit sss) {

        //}
        payment1.setUnit(new Unit());
        paymentRepository.save(payment1);
    }

    public void editPayment(Payment payment) {
    }
}
