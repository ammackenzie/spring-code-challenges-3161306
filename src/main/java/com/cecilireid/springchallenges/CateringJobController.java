package com.cecilireid.springchallenges;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("cateringJobs")
public class CateringJobController {
    private static final String IMAGE_API = "https://foodish-api.herokuapp.com";
    private final CateringJobRepository cateringJobRepository;
    WebClient client;

    public CateringJobController(CateringJobRepository cateringJobRepository, WebClient.Builder webClientBuilder) {
        this.cateringJobRepository = cateringJobRepository;
        this.client = webClientBuilder.baseUrl(IMAGE_API)
                .build();
    }

    @GetMapping
    @ResponseBody
    public List<CateringJob> getCateringJobs() {
        return cateringJobRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CateringJob getCateringJobById(@PathVariable Long id) {
        if (cateringJobRepository.existsById(id)) {
            return cateringJobRepository.findById(id)
                    .get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No catering job found for " + id);
        }
    }

    @GetMapping("/findByStatus")
    @ResponseBody
    public List<CateringJob> getCateringJobsByStatus(@RequestParam Status status) {
        return cateringJobRepository.findAllByStatus(status);
    }

    @PostMapping
    @ResponseBody
    public CateringJob createCateringJob(@RequestBody CateringJob cateringJob) {
        return cateringJobRepository.save(cateringJob);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public CateringJob updateCateringJob(@RequestBody CateringJob cateringJob, @PathVariable Long id) {
        if (cateringJobRepository.existsById(id)) {
            cateringJob.setId(id);
            return cateringJobRepository.save(cateringJob);
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No catering job found for " + id);
        }
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public CateringJob patchCateringJob(@PathVariable Long id, @RequestBody JsonNode json) {
        Optional<CateringJob> optionalJob = cateringJobRepository.findById(id);
        if (optionalJob.isPresent()) {
            CateringJob currentJob = optionalJob.get();
            JsonNode menu = json.get("menu");
            if (menu != null) {
                currentJob.setMenu(menu.asText());
                return cateringJobRepository.save(currentJob);
            } else {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No catering job found for " + id);
        }
    }

    @GetMapping("/surpriseMe")
    public Mono<String> getSurpriseImage() {
        return client.get()
                .uri("/api")
                .retrieve()
                .bodyToMono(String.class);
    }
}
