package ibf2022.ssf.assessment.controllers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.ssf.assessment.models.Cart;
import ibf2022.ssf.assessment.models.Quotation;
import ibf2022.ssf.assessment.services.QuotationService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/add")
public class PurchaseOrderController {
    @Autowired
    QuotationService quotationSVC;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String postMapping(@RequestBody MultiValueMap<String, String> form, Model model, HttpSession session) {

        String item = form.getFirst("item");
        String quantity = form.getFirst("quantity");
        Map<String, Integer> cartSession = (Map<String, Integer>) session.getAttribute("cartSession");
        Cart cart;

        if (null == cartSession || cartSession.isEmpty()) {
            // If cart is null, then new session
            cartSession = new HashMap<>();
            session.setAttribute("cartSession", cartSession);
            cart = new Cart(item, Integer.parseInt(quantity));
        }
        Integer count = cartSession.containsKey(item) ? cartSession.get(item) : 0;
        cartSession.put(item, count + Integer.parseInt(quantity));
        cart = new Cart(item, count + Integer.parseInt(quantity));

        model.addAttribute("cartSession", cartSession);
        System.out.printf(">>> Cart: %s\n", cartSession.toString());

        return "view1";
    }

    @GetMapping(path = "/shippingaddress")
    public String purchaseOrder(HttpSession session) {
        Map<String, Integer> cartSession = (Map<String, Integer>) session.getAttribute("cartSession");

        if (null == cartSession || cartSession.isEmpty()) {
            // If cart is null, then new session
            System.out.println("Empty here");
            return "view1";
        }

        return "view2";
    }

    @PostMapping(path = "/quotation", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getQuotations(@RequestBody MultiValueMap<String, String> form, Model model, HttpSession session)
            throws Exception {

        String name = form.getFirst("name");
        String address = form.getFirst("address");

        // POST /quotation
        // Accept: appplication/json
        // Content-Type: application/json

        Map<String, Integer> cartSession = (Map<String, Integer>) session.getAttribute("cartSession");

        List<String> itemList = new LinkedList<>();
        List<Integer> itemQuantity = new LinkedList<>();
        Float totalCost = 0.0f;

        System.out.print("COST FIRST: $" + totalCost + "\n");
        for (Map.Entry<String, Integer> entry : cartSession.entrySet()) {
            // System.out.println(entry.getKey().toString());
            // System.out.println(quotation.getQuotation(entry.getKey().toString()));
            // totalCost += quotation.getQuotation(entry.getKey().toString()) *
            // entry.getValue();
            itemList.add(entry.getKey());
            itemQuantity.add(entry.getValue());
        }
        Quotation quotation = quotationSVC.getQuotations(itemList);
        for (int i = 0; i < itemList.size(); i++) {
            totalCost += quotation.getQuotation(itemList.get(i)) * itemQuantity.get(i);
        }

        System.out.print("COST LAST: $" + totalCost);

        model.addAttribute("invoiceId", quotation.getQuoteId());
        model.addAttribute("name", name);
        model.addAttribute("address", address);
        model.addAttribute("cost", totalCost);

        session.invalidate();

        return "view3";
    }
}
