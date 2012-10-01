package com.leafnoise.pathfinder.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import com.leafnoise.pathfinder.model.Message;
import com.leafnoise.pathfinder.service.MessageService;

@Named("mainBB")
public class MainBB {

	@Inject 
	@Named("mongoMessageService")
	private MessageService mms;
	
	public String doSth(){
		if(mms!=null){
			Message m = new Message();
			m.setOrigin("Jorge");
			m.setReceivedDate(new Date());
			m.setPayload("{\"clave\":\"valor\"}");
			mms.save(m);
			return "index?faces-redirect=true";
		}else{
			return "index?faces-redirect=true";
		}
	}
}
