package com.leafnoise.pathfinder.controller;

import javax.inject.Inject;
import javax.inject.Named;

import com.leafnoise.pathfinder.service.MessageService;

@Named("mainBB")
public class MainBB {

	@Inject 
	@Named("mongoMessageService")
	private MessageService mms;
	
}
