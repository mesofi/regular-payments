package com.mesofi.payments.views;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.repository.CrudRepository;

import com.mesofi.payments.entity.Payment;
import com.mesofi.payments.repository.PaymentRepository;
import com.mesofi.payments.repository.UnitRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PageTitle("Payments")
@Route(value = "ui")
public class PaymentsListingView extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = -3301431192190235841L;

    private final PaymentRepository paymentRepository;
    private final UnitRepository unitRepository;
    private final Grid<Payment> grid = new Grid<>();
    private PaymentsForm form;

    public PaymentsListingView(PaymentRepository paymentRepository, UnitRepository unitRepository) {
        this.paymentRepository = paymentRepository;
        this.unitRepository = unitRepository;

        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolBar(), getContent());
        populateGrid();
        closeEditorForm();
    }

    private void closeEditorForm() {
        form.setPaymentView(null);
        form.setVisible(false);
    }

    private void configureForm() {
        form = new PaymentsForm(getAllRecords(unitRepository));
        form.setWidth("25em");
        form.addSaveListener(this::savePayment);
        form.addDeleteListener(this::deleteFigure);
        form.addCloseListener(e -> closeEditor());
    }

    private void closeEditor() {
        form.setPaymentView(null);
        form.setVisible(false);
    }

    private void savePayment(PaymentsForm.SaveEvent event) {
        Payment payment = event.getPayment();
        long id = payment.getId();
        if (id == 0) {
            log.debug("A new payment will be created");
        } else {
            log.debug("Existing payment with id: {} will be updated", id);
        }
        paymentRepository.save(payment);
        populateGrid();
        closeEditor();
    }

    private void deleteFigure(PaymentsForm.DeleteEvent event) {
        Payment payment = event.getPayment();
        log.debug("Deleting a payment record with id: {}", payment.getId());

        paymentRepository.delete(payment);
        populateGrid();
        closeEditor();
    }

    private void populateGrid() {
        populateGrid(getAllRecords(paymentRepository));
    }

    private void populateGrid(List<Payment> items) {
        grid.setItems(items);
    }

    private <T> List<T> getAllRecords(CrudRepository<T, Long> crudRepository) {
        List<T> allRecords = new ArrayList<>();
        crudRepository.findAll().forEach(allRecords::add);
        return allRecords;
    }

    private Component getToolBar() {
        Button button = new Button("Add new Payment");
        button.addClickListener(e -> addNewPayment());
        HorizontalLayout toolBar = new HorizontalLayout(button);
        toolBar.addClassName("figure-toolbar-class");
        return toolBar;
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addColumn(Payment::getUnit).setHeader("Unit");
        grid.addColumn(Payment::getAmount).setHeader("Amount");
        grid.addColumn(Payment::getPaymentDate).setHeader("Payment Date");
        grid.addColumn(Payment::getRemarks).setHeader("Additional Information");

        grid.getColumns().forEach($ -> $.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editPayment(e.getValue()));
    }

    private void addNewPayment() {
        grid.asSingleSelect().clear();
        editPayment(new Payment());
    }

    private void editPayment(Payment selectedPayment) {
        if (Objects.isNull(selectedPayment)) {
            closeEditor();
        } else {
            form.setPaymentView(selectedPayment);
            form.setVisible(true);
        }
    }
}
