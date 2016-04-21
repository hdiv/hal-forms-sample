package sample.halforms.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sample.halforms.resource.EntryPointResource;

@Controller
public class ApplicationController {

	@RequestMapping(value = "/", produces = { "application/json" })
	@ResponseBody
	public EntryPointResource index() {

		EntryPointResource entryPoint = new EntryPointResource();
		entryPoint.add(linkTo(methodOn(TaskController.class).list()).withRel("tasks"));

		return entryPoint;
	}

	@RequestMapping(value = "/", produces = { "text/html" })
	public String indexPage() {
		return "redirect:/browser/index.html";
	}
}
