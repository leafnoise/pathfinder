package com.leafnoise.pathfinder.bbs;

import javax.inject.Inject;
import javax.inject.Named;

import com.leafnoise.pathfinder.service.MessageService;

@Named("mainBB")
public class MainBB {

	@Inject 
	@Named("mongoMessageService")
	private MessageService mms;
	
}
