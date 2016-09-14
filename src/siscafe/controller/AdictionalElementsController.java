/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import siscafe.model.AdictionalElements;
import siscafe.view.frontend.AdictionalElementsView;

/**
 *
 * @author Administrador
 */
public class AdictionalElementsController {
    
    public AdictionalElementsController(AdictionalElementsView adictionalElementsView, AdictionalElements adictionalElements){
        this.adictionalElementsView = adictionalElementsView;
        this.adictionalElements = adictionalElements;
    }
    
    public void initListener(){
        
    }
    
    private AdictionalElementsView adictionalElementsView;
    private AdictionalElements adictionalElements;
}
