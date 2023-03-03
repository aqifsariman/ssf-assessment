// package ibf2022.ssf.assessment.controllers;

// import java.util.Map;
// import java.util.logging.Logger;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import ibf2022.ssf.assessment.models.Quotation;
// import ibf2022.ssf.assessment.services.QuotationService;
// import jakarta.servlet.http.HttpSession;

// @RestController
// @RequestMapping(path = "/quotation")
// public class QuotationRestController {

// // POST /quotation
// // Accept: appplication/json
// // Content-Type: application/json

// @Autowired
// QuotationService quotationSVC;

// private Logger logger =
// Logger.getLogger(QuotationRestController.class.getName());

// @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
// public ResponseEntity<Quotation> getQuotation(HttpSession session) {
// Map<String, Integer> cartSession = (Map<String, Integer>)
// session.getAttribute("cartSession");

// Quotation quotation = quotationSVC.getQuotations(null)

// }
// }
