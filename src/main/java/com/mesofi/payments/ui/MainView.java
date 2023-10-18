package com.mesofi.payments.ui;

import java.util.ArrayList;
import java.util.List;

import com.mesofi.payments.entity.Payment;
import com.mesofi.payments.entity.Unit;
import com.mesofi.payments.repository.PaymentRepository;
import com.mesofi.payments.repository.UnitRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("ui")
public class MainView extends VerticalLayout {

    private static final long serialVersionUID = 3395572794044704884L;

    private final PaymentRepository paymentRepository;
    private final PaymentForm form;

    final Grid<Payment> grid;
    private final Button addNewBtn;

    public MainView(PaymentRepository paymentRepository, UnitRepository unitRepository) {
        this.paymentRepository = paymentRepository;

        List<Unit> allUnits = new ArrayList<>();
        unitRepository.findAll().forEach(allUnits::add);
        this.form = new PaymentForm(allUnits, paymentRepository);

        this.grid = new Grid<>(Payment.class);
        this.grid.setColumns("amount", "paymentDate", "unit");

        this.addNewBtn = new Button("New Payment", VaadinIcon.PLUS.create());
        this.addNewBtn.addClickListener(e -> {
            System.out.println("============");
        });

        // build layout
        HorizontalLayout actions = new HorizontalLayout(addNewBtn);

        add(actions, grid, form);
        listCustomers();

    }

    private void listCustomers() {
        Iterable<Payment> iterable = paymentRepository.findAll();
        List<Payment> list = new ArrayList<>();
        iterable.forEach(list::add);
        grid.setItems(list);
    }
}
