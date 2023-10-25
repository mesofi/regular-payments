package com.mesofi.payments.views;

import java.io.Serial;
import java.util.List;

import com.mesofi.payments.entity.Payment;
import com.mesofi.payments.entity.Unit;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import lombok.Getter;

public class PaymentForm extends FormLayout {

    @Serial
    private static final long serialVersionUID = -8310823663213359590L;

    Binder<Payment> binder = new BeanValidationBinder<>(Payment.class);
    Payment payment;

    ComboBox<Unit> unit = new ComboBox<>("Existing Units");
    TextField amount = new TextField("Amount");
    DateTimePicker paymentDate = new DateTimePicker("Payment Date");
    TextArea remarks = new TextArea("Additional information");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    public PaymentForm(List<Unit> allUnits) {
        binder.bindInstanceFields(this);

        unit.setItems(allUnits);
        unit.setItemLabelGenerator(Unit::getNumber);

        add(unit, amount, paymentDate, remarks, createButtonLayout());
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        binder.readBean(payment);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, payment)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(payment);
            fireEvent(new SaveEvent(this, payment));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    @Getter
    public static abstract class FigureFormEvent extends ComponentEvent<PaymentForm> {
        @Serial
        private static final long serialVersionUID = 6476547532881871346L;
        private final Payment payment;

        protected FigureFormEvent(PaymentForm source, Payment payment) {
            super(source, false);
            this.payment = payment;
        }
    }

    public static class SaveEvent extends FigureFormEvent {
        @Serial
        private static final long serialVersionUID = 7279050229708181166L;

        SaveEvent(PaymentForm source, Payment payment) {
            super(source, payment);
        }
    }

    public static class DeleteEvent extends FigureFormEvent {
        @Serial
        private static final long serialVersionUID = 2310452970737431414L;

        DeleteEvent(PaymentForm source, Payment payment) {
            super(source, payment);
        }

    }

    public static class CloseEvent extends FigureFormEvent {
        @Serial
        private static final long serialVersionUID = -8638191020626686710L;

        CloseEvent(PaymentForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
