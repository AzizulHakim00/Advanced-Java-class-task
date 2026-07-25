package bd.edu.seu.printpulse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/printpulse")
public class PrintRequestController {

    private final PrintRequestInterface printRequestInterface;

    @ModelAttribute("printTypes")
    public List<String> printTypes() {
        return List.of("Black & White", "Color");
    }

    @ModelAttribute("paperSizes")
    public List<String> paperSizes() {
        return List.of("A4", "A3", "Letter");
    }

    @ModelAttribute("statuses")
    public List<String> statuses() {
        return List.of("Waiting", "Printing", "Ready", "Collected", "Cancelled");
    }

    @GetMapping
    public String home() {
        return "redirect:/printpulse/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<PrintRequest> requests = printRequestInterface.findAll();

        long waitingCount = countStatus(requests, "Waiting");
        long printingCount = countStatus(requests, "Printing");
        long readyCount = countStatus(requests, "Ready");
        long collectedCount = countStatus(requests, "Collected");

        long todayCount = requests.stream()
                .filter(request -> LocalDate.now().equals(request.getRequestDate()))
                .count();

        double totalRevenue = requests.stream()
                .filter(request -> "Collected".equalsIgnoreCase(request.getStatus()))
                .mapToDouble(PrintRequest::getTotalCost)
                .sum();

        List<PrintRequest> activeQueue = requests.stream()
                .filter(request -> !"Collected".equalsIgnoreCase(request.getStatus()))
                .filter(request -> !"Cancelled".equalsIgnoreCase(request.getStatus()))
                .sorted(Comparator.comparing(PrintRequest::getRequestId))
                .limit(6)
                .toList();

        model.addAttribute("totalCount", requests.size());
        model.addAttribute("todayCount", todayCount);
        model.addAttribute("waitingCount", waitingCount);
        model.addAttribute("printingCount", printingCount);
        model.addAttribute("readyCount", readyCount);
        model.addAttribute("collectedCount", collectedCount);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("activeQueue", activeQueue);

        return "dashboard";
    }

    @GetMapping("/requests/add")
    public String showPrintForm(Model model) {
        PrintRequest printRequest = new PrintRequest();
        printRequest.setCopies(1);
        printRequest.setPrintType("Black & White");
        printRequest.setPaperSize("A4");
        printRequest.setStatus("Waiting");
        printRequest.setRequestDate(LocalDate.now());

        model.addAttribute("name", "New Print Request");
        model.addAttribute("printRequest", printRequest);
        model.addAttribute("editMode", false);

        return "form";
    }

    @PostMapping("/requests/add")
    public String addPrintRequest(
            @Valid @ModelAttribute("printRequest") PrintRequest printRequest,
            BindingResult bindingResult,
            Model model) {

        if (printRequest.getRequestId() != null
                && printRequestInterface.existsById(printRequest.getRequestId())) {
            bindingResult.rejectValue("requestId", "duplicate.requestId", "This Request ID already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "New Print Request");
            model.addAttribute("editMode", false);
            return "form";
        }

        updateCollectedDate(printRequest);
        printRequestInterface.save(printRequest);
        log.info("Print request added: {}", printRequest);

        return "redirect:/printpulse/requests";
    }

    @GetMapping("/requests")
    public String showPrintQueue(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String printType,
            Model model) {

        List<PrintRequest> filteredRequests = printRequestInterface.findAll().stream()
                .filter(request -> {
                    boolean searchMatch = keyword == null
                            || keyword.isBlank()
                            || request.getRequestId().toString().contains(keyword)
                            || request.getStudentId().toLowerCase().contains(keyword.toLowerCase())
                            || request.getStudentName().toLowerCase().contains(keyword.toLowerCase())
                            || request.getDocumentName().toLowerCase().contains(keyword.toLowerCase());

                    boolean statusMatch = status == null
                            || status.isBlank()
                            || request.getStatus().equalsIgnoreCase(status);

                    boolean printTypeMatch = printType == null
                            || printType.isBlank()
                            || request.getPrintType().equalsIgnoreCase(printType);

                    return searchMatch && statusMatch && printTypeMatch;
                })
                .sorted(Comparator.comparing(PrintRequest::getRequestId))
                .toList();

        model.addAttribute("requests", filteredRequests);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPrintType", printType);

        return "list";
    }

    @GetMapping("/requests/details/{requestId}")
    public String showDetails(@PathVariable Integer requestId, Model model) {
        PrintRequest printRequest = printRequestInterface.findById(requestId).orElse(null);

        if (printRequest == null) {
            return "redirect:/printpulse/requests";
        }

        model.addAttribute("printRequest", printRequest);
        return "details";
    }

    @GetMapping("/requests/edit/{requestId}")
    public String showEditForm(@PathVariable Integer requestId, Model model) {
        PrintRequest existingRequest = printRequestInterface.findById(requestId).orElse(null);

        if (existingRequest == null) {
            return "redirect:/printpulse/requests";
        }

        model.addAttribute("name", "Edit Print Request");
        model.addAttribute("printRequest", existingRequest);
        model.addAttribute("editMode", true);
        model.addAttribute("originalId", requestId);

        return "form";
    }

    @PostMapping("/requests/edit/{requestId}")
    public String updatePrintRequest(
            @PathVariable Integer requestId,
            @Valid @ModelAttribute("printRequest") PrintRequest printRequest,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "Edit Print Request");
            model.addAttribute("editMode", true);
            model.addAttribute("originalId", requestId);
            return "form";
        }

        PrintRequest existingRequest = printRequestInterface.findById(requestId).orElse(null);

        if (existingRequest == null) {
            return "redirect:/printpulse/requests";
        }

        printRequest.setRequestId(requestId);
        printRequest.setCollectedDate(existingRequest.getCollectedDate());
        updateCollectedDate(printRequest);
        printRequestInterface.save(printRequest);

        log.info("Print request updated: {}", printRequest);
        return "redirect:/printpulse/requests";
    }

    @GetMapping("/requests/status/{requestId}")
    public String updateStatus(
            @PathVariable Integer requestId,
            @RequestParam String value) {

        PrintRequest printRequest = printRequestInterface.findById(requestId).orElse(null);

        if (printRequest != null && statuses().contains(value)) {
            printRequest.setStatus(value);
            updateCollectedDate(printRequest);
            printRequestInterface.save(printRequest);
            log.info("Print request status updated. ID: {}, status: {}", requestId, value);
        }

        return "redirect:/printpulse/requests";
    }

    @GetMapping("/requests/delete/{requestId}")
    public String deletePrintRequest(@PathVariable Integer requestId) {
        if (printRequestInterface.existsById(requestId)) {
            printRequestInterface.deleteById(requestId);
            log.info("Print request deleted. ID: {}", requestId);
        }

        return "redirect:/printpulse/requests";
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        List<PrintRequest> collectedRequests = printRequestInterface.findAll().stream()
                .filter(request -> "Collected".equalsIgnoreCase(request.getStatus()))
                .sorted(Comparator.comparing(
                        PrintRequest::getCollectedDate,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .toList();

        int totalPrintedPages = collectedRequests.stream()
                .mapToInt(PrintRequest::getTotalPrintedPages)
                .sum();

        double totalRevenue = collectedRequests.stream()
                .mapToDouble(PrintRequest::getTotalCost)
                .sum();

        long colorJobs = collectedRequests.stream()
                .filter(request -> "Color".equalsIgnoreCase(request.getPrintType()))
                .count();

        model.addAttribute("collectedRequests", collectedRequests);
        model.addAttribute("completedCount", collectedRequests.size());
        model.addAttribute("totalPrintedPages", totalPrintedPages);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("colorJobs", colorJobs);

        return "history";
    }

    private long countStatus(List<PrintRequest> requests, String status) {
        return requests.stream()
                .filter(request -> status.equalsIgnoreCase(request.getStatus()))
                .count();
    }

    private void updateCollectedDate(PrintRequest printRequest) {
        if ("Collected".equalsIgnoreCase(printRequest.getStatus())) {
            if (printRequest.getCollectedDate() == null) {
                printRequest.setCollectedDate(LocalDate.now());
            }
        } else {
            printRequest.setCollectedDate(null);
        }
    }
}
