package com.mesofi.payments.views;

import java.io.Serial;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.mesofi.payments.entity.Payment;
import com.mesofi.payments.entity.Unit;
import com.mesofi.payments.repository.PaymentRepository;
import com.mesofi.payments.repository.UnitRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Payments")
@Route(value = "ui")
public class PaymentsView extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 1139195313256274726L;

    private final Grid<Payment> grid = new Grid<>();
    private PaymentForm form;

    private final PaymentRepository paymentRepository;
    private final UnitRepository unitRepository;

    public PaymentsView(PaymentRepository paymentRepository, UnitRepository unitRepository) {
        this.paymentRepository = paymentRepository;
        this.unitRepository = unitRepository;

        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolBar(), getContent());
        populateGrid();
        closeEditor();
    }

    private void closeEditor() {
        form.setPayment(null);
        form.setVisible(false);
    }

    private void populateGrid() {
        Iterable<Payment> allPayments = paymentRepository.findAll();
        List<Payment> paymentList = new ArrayList<>();
        allPayments.forEach(paymentList::add);
        grid.setItems(paymentList);
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        Iterable<Unit> allUnits = unitRepository.findAll();
        List<Unit> unitList = new ArrayList<>();
        allUnits.forEach(unitList::add);

        form = new PaymentForm(unitList);
        form.setWidth("25em");
        form.addSaveListener(this::savePayment);
        form.addDeleteListener(this::deletePayment);
        form.addCloseListener(e -> closeEditor());
    }

    private void savePayment(PaymentForm.SaveEvent event) {
        Payment payment = event.getPayment();
        paymentRepository.save(payment);
        populateGrid();
        closeEditor();
    }

    private void deletePayment(PaymentForm.DeleteEvent deleteEvent) {
        System.out.println("DELETED");
    }

    private Component getToolBar() {
        Button button = new Button("Add new Payment");
        button.addClickListener(e -> addNewFigure());
        return new HorizontalLayout(button);
    }

    private void addNewFigure() {
        grid.asSingleSelect().clear();
        editPayment(new Payment());
    }

    private void configureGrid() {
        final DateTimeFormatter lStyle = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

        grid.setSizeFull();

        grid.addColumn(Payment::getUnit).setHeader("Unit").setFlexGrow(0).setSortable(true);

        Locale mxnLocale = new Locale("mx", "MX");
        grid.addColumn($ -> String.format(mxnLocale, "MXN %,.0f", $.getAmount())).setHeader("Amount").setSortable(true);

        grid.addColumn($ -> $.getPaymentDate().format(lStyle)).setHeader("Release Date").setSortable(true)
                .setComparator(Payment::getPaymentDate);

        grid.getColumns().forEach($ -> $.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editPayment(e.getValue()));
    }

    private void editPayment(Payment value) {
        if (Objects.isNull(value)) {
            closeEditor();
        } else {
            form.setPayment(value);
            form.setVisible(true);
        }
    }

}
