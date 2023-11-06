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
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentsForm extends FormLayout {

    @Serial
    private static final long serialVersionUID = 8033307388790626168L;

    Payment selectedPayment;
    Binder<Payment> binder = new BeanValidationBinder<>(Payment.class);

    // Form fields
    ComboBox<Unit> unit = new ComboBox<>("Select a Unit");
    TextField amount = new TextField("Amount");
    DatePicker paymentDate = new DatePicker("Payment Date");
    TextArea remarks = new TextArea("Additional information");

    // Command buttons
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    public PaymentsForm(List<Unit> allUnits) {
        binder.bindInstanceFields(this);

        unit.setItems(allUnits);
        unit.setItemLabelGenerator(Unit::getNumber);

        add(unit, amount, paymentDate, remarks, createButtonLayout());
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, selectedPayment)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, cancel);
    }

    public void setPaymentView(Payment selectedPayment) {
        this.selectedPayment = selectedPayment;
        binder.readBean(selectedPayment);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(selectedPayment);
            fireEvent(new SaveEvent(this, selectedPayment));
        } catch (ValidationException e) {
            log.error("Error while validating and saving the payment", e);
        }
    }

    // Events
    @Getter
    public static abstract class PaymentsFormEvent extends ComponentEvent<PaymentsForm> {
        @Serial
        private static final long serialVersionUID = 6476547532881871346L;
        private final Payment payment;

        protected PaymentsFormEvent(PaymentsForm source, Payment payment) {
            super(source, false);
            this.payment = payment;
        }
    }

    public static class SaveEvent extends PaymentsFormEvent {
        @Serial
        private static final long serialVersionUID = 7279050229708181166L;

        SaveEvent(PaymentsForm source, Payment payment) {
            super(source, payment);
        }
    }

    public static class DeleteEvent extends PaymentsFormEvent {
        @Serial
        private static final long serialVersionUID = 2310452970737431414L;

        DeleteEvent(PaymentsForm source, Payment payment) {
            super(source, payment);
        }

    }

    public static class CloseEvent extends PaymentsFormEvent {
        @Serial
        private static final long serialVersionUID = -8638191020626686710L;

        CloseEvent(PaymentsForm source) {
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
