package com.example.srewebsite.controller;

import com.example.srewebsite.model.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class SreController {

    @GetMapping("/")
    public String showSreTools(Model model) {
        model.addAttribute("monitoringTools", getMonitoringTools());
        model.addAttribute("iacTools", getIacTools());
        model.addAttribute("containerTools", getContainerTools());
        model.addAttribute("ciCdTools", getCiCdTools());
        model.addAttribute("cloudPlatforms", getCloudPlatforms());
        return "sre";
    }

    private List<Tool> getMonitoringTools() {
        return Arrays.asList(
            new Tool("Prometheus", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#E6522C' d='M124.3 114.8c1.8-2.5 2.9-5.5 2.9-8.8V22c0-7.7-6.3-14-14-14H22c-3.3 0-6.4 1.1-8.8 2.9l111.1 111.1z'/><path fill='#E6522C' d='M3.7 114.8c-1.8-2.5-2.9-5.5-2.9-8.8V22c0-7.7 6.3-14 14-14h84c3.3 0 6.4 1.1 8.8 2.9L3.7 114.8z'/><path fill='#FFFFFF' d='M64 126.5c34.5 0 62.5-28 62.5-62.5S98.5 1.5 64 1.5 1.5 29.5 1.5 64s28 62.5 62.5 62.5zm0-16c-25.7 0-46.5-20.8-46.5-46.5S38.3 17.5 64 17.5s46.5 20.8 46.5 46.5-20.8 46.5-46.5 46.5z'/><path fill='#FFFFFF' d='M64 110.5c25.7 0 46.5-20.8 46.5-46.5S89.7 17.5 64 17.5 17.5 38.3 17.5 64s20.8 46.5 46.5 46.5zm0-16c-16.8 0-30.5-13.7-30.5-30.5S47.2 33.5 64 33.5s30.5 13.7 30.5 30.5-13.7 30.5-30.5 30.5z'/><path fill='#FFFFFF' d='M64 94.5c16.8 0 30.5-13.7 30.5-30.5S80.8 33.5 64 33.5 33.5 47.2 33.5 64 47.2 94.5 64 94.5z'/></svg>", 
                "Open-source systems monitoring and alerting toolkit"),
            new Tool("Grafana", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#F46800' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#F46800' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Open platform for analytics and monitoring"),
            new Tool("Datadog", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#632CA6' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#632CA6' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Monitoring service for cloud-scale applications"),
            new Tool("New Relic", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#008C99' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#008C99' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Application performance monitoring tool"),
            new Tool("Splunk", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#000000' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#000000' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Platform for searching, monitoring, and analyzing machine data"),
            new Tool("ELK Stack", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#005571' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#005571' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Elasticsearch, Logstash, Kibana for log analysis")
        );
    }

    private List<Tool> getIacTools() {
        return Arrays.asList(
            new Tool("Terraform", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#844FBA' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#844FBA' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Infrastructure as code tool by HashiCorp"),
            new Tool("Ansible", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#EE0000' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#EE0000' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Configuration management and automation"),
            new Tool("Pulumi", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#8A3391' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#8A3391' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Modern infrastructure as code using real languages"),
            new Tool("Chef", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#F09820' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#F09820' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Configuration management tool")
        );
    }

    private List<Tool> getContainerTools() {
        return Arrays.asList(
            new Tool("Docker", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#2496ED' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#2496ED' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Containerization platform"),
            new Tool("Kubernetes", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#326CE5' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#326CE5' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Container orchestration system"),
            new Tool("Helm", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#0F1689' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#0F1689' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Package manager for Kubernetes"),
            new Tool("Istio", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#466BB0' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#466BB0' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Service mesh for microservices")
        );
    }

    private List<Tool> getCiCdTools() {
        return Arrays.asList(
            new Tool("Jenkins", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#D24939' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#D24939' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Open-source automation server"),
            new Tool("GitHub Actions", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#2088FF' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#2088FF' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "CI/CD platform integrated with GitHub"),
            new Tool("GitLab CI/CD", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#FC6D26' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#FC6D26' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Built-in continuous integration in GitLab"),
            new Tool("Argo CD", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#EF7B4D' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#EF7B4D' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Declarative GitOps continuous delivery tool")
        );
    }

    private List<Tool> getCloudPlatforms() {
        return Arrays.asList(
            new Tool("AWS", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#FF9900' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#FF9900' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Amazon Web Services cloud platform"),
            new Tool("Google Cloud", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#4285F4' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#4285F4' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Google Cloud Platform"),
            new Tool("Azure", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#0078D4' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#0078D4' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Microsoft's cloud platform"),
            new Tool("DigitalOcean", 
                "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 128 128' class='logo-svg'><path fill='#0080FF' d='M64 128c35.3 0 64-28.7 64-64S99.3 0 64 0 0 28.7 0 64s28.7 64 64 64z'/><path fill='#FFFFFF' d='M96 64c0 17.7-14.3 32-32 32S32 81.7 32 64s14.3-32 32-32 32 14.3 32 32z'/><path fill='#0080FF' d='M64 96c17.7 0 32-14.3 32-32S81.7 32 64 32 32 46.3 32 64s14.3 32 32 32z'/><path fill='#FFFFFF' d='M64 80c8.8 0 16-7.2 16-16s-7.2-16-16-16-16 7.2-16 16 7.2 16 16 16z'/></svg>", 
                "Cloud infrastructure provider")
        );
    }
}
