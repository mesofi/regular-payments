package com.mesofi.payments.ui;

import java.io.Serial;
import java.util.List;

import com.mesofi.payments.entity.Payment;
import com.mesofi.payments.entity.Unit;
import com.mesofi.payments.repository.PaymentRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

public class PaymentForm extends FormLayout {

    @Serial
    private static final long serialVersionUID = 1377344767293042900L;

    TextField amount = new TextField("Payment Amount");
    ComboBox<Unit> unit = new ComboBox<>("Unit");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Payment> binder = new BeanValidationBinder<>(Payment.class);

    private final PaymentRepository repository;

    public PaymentForm(List<Unit> units, PaymentRepository repository) {
        this.repository = repository;
        binder.bindInstanceFields(this);
        unit.setItems(units);
        unit.setItemLabelGenerator(Unit::getNumber);
        add(amount, unit, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);

        save.addClickListener($ -> {
            Payment newPayment = new Payment();
            
            
            repository.save(newPayment);
        });

        return new HorizontalLayout(save, delete, close);
    }
}
