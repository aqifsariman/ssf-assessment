package ibf2022.ssf.assessment.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.ssf.assessment.models.Cart;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/add")
public class PurchaseOrderController {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String postMapping(@RequestBody MultiValueMap<String, String> form, Model model, HttpSession session) {

        String item = form.getFirst("item");
        String quantity = form.getFirst("quantity");
        Map<String, Integer> cartSession = (Map<String, Integer>) session.getAttribute("cartSession");
        Cart cart;

        if (null == cartSession || cartSession.isEmpty()) {
            // If cart is null, then new session
            System.out.println("Checking here");
            cartSession = new HashMap<>();
            session.setAttribute("cartSession", cartSession);
            cart = new Cart(item, Integer.parseInt(quantity));
        }
        Integer count = cartSession.containsKey(item) ? cartSession.get(item) : 0;
        cartSession.put(item, count + Integer.parseInt(quantity));
        cart = new Cart(item, count + Integer.parseInt(quantity));

        model.addAttribute("cartSession", cartSession);
        System.out.printf(">>> Cart: %s\n", cartSession.toString());
        System.out.println((Map<String, Integer>) session.getAttribute("cartSession"));

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

}
