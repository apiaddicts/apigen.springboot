package net.cloudappi.apigen.archetypecore.interceptors.controller;

import net.cloudappi.apigen.archetypecore.interceptors.expand.ApigenExpand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expand")
public class FakeExpandController {

    @GetMapping("/allowed")
    public void getAllowed() {
    }

    @GetMapping("/excluded")
    public void getExcluded() {
    }

    @GetMapping("/generic")
    public void getGeneric() {
    }

    @GetMapping("/level")
    public void getLevel() {
    }

    @GetMapping("/trailing-slash")
    public void getTrailingSlash() {
    }

    @GetMapping("/expression")
    public void getExpression() {
    }

    @GetMapping("/expression/explicit")
    public void getExpressionExplicit() {
    }

    @GetMapping("/expression/implicit")
    public void getExpressionImplicit() {
    }

    @ApigenExpand(allowed = {"one", "one.two", "one.two.three"})
    @GetMapping("/annotation/allowed")
    public void getAnnotationAllowed() {
    }

    @ApigenExpand(excluded = {"one", "one.two", "one.two.three"})
    @GetMapping("/annotation/excluded")
    public void getAnnotationExcluded() {
    }

    @ApigenExpand(excluded = {"one", "one.two", "one.two.three"}, maxLevel = 3)
    @GetMapping("/annotation/excluded-and-level")
    public void getAnnotationExcludedAndLevel() {
    }

    @ApigenExpand(maxLevel = 3)
    @GetMapping("/annotation/level")
    public void getAnnotationLevel() {
    }
}