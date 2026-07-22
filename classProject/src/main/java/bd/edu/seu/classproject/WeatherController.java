package bd.edu.seu.classproject;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping("/weather")
public class WeatherController {

    private final List<Weather> weathers = new ArrayList<>();

    @GetMapping("/add")
    public String showWeatherForm(Model model) {

        model.addAttribute("name", "Add Weather");
        model.addAttribute("weather", new Weather());
        model.addAttribute("editMode", false);

        return "form";
    }

    @PostMapping("/add")
    public String addWeather(
            @Valid @ModelAttribute("weather") Weather weather,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "Add Weather");
            model.addAttribute("editMode", false);

            return "form";
        }

        weathers.add(weather);

        log.info("Weather added: {}", weather);

        return "redirect:/weather/list";
    }

    @GetMapping("/list")
    public String showListWeather(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String moisture,
            Model model) {

        List<Weather> filteredWeathers = weathers.stream()
                .filter(weather -> {

                    boolean searchMatch = keyword == null
                            || keyword.isBlank()
                            || weather.getDay().toLowerCase()
                            .contains(keyword.toLowerCase());

                    boolean moistureMatch = moisture == null
                            || moisture.isBlank()
                            || weather.getMoisture().toString()
                            .contains(moisture);

                    return searchMatch && moistureMatch;
                })
                .toList();

        model.addAttribute("weathers", filteredWeathers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moisture", moisture);

        return "list";
    }

    @GetMapping("/edit/{day}")
    public String showEditForm(
            @PathVariable String day,
            Model model) {

        Weather existingWeather = weathers.stream()
                .filter(weather ->
                        Objects.equals(weather.getDay(), day))
                .findFirst()
                .orElse(null);

        if (existingWeather == null) {
            return "redirect:/weather/list";
        }

        model.addAttribute("name", "Edit Weather");
        model.addAttribute("weather", existingWeather);
        model.addAttribute("editMode", true);
        model.addAttribute("originalDay", day);

        return "form";
    }

    @PostMapping("/edit/{day}")
    public String updateWeather(
            @PathVariable String day,
            @Valid @ModelAttribute("weather") Weather weather,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("name", "Edit Weather");
            model.addAttribute("editMode", true);
            model.addAttribute("originalDay", day);

            return "form";
        }

        for (int i = 0; i < weathers.size(); i++) {

            if (Objects.equals(weathers.get(i).getDay(), day)) {

                weathers.set(i, weather);

                log.info("Weather updated: {}", weather);

                break;
            }
        }

        return "redirect:/weather/list";
    }

    @GetMapping("/delete/{day}")
    public String deleteWeather(@PathVariable String day) {

        weathers.removeIf(weather ->
                Objects.equals(weather.getDay(), day));

        log.info("Weather deleted. Day: {}", day);

        return "redirect:/weather/list";
    }
}