import { Component, OnInit } from '@angular/core';
import { AgGridModule } from 'ag-grid-angular';
import * as $ from 'jquery';
import { GlobalServiceService } from '../global-service.service';

@Component({
  selector: 'app-sidebarnavigation',
  templateUrl: './sidebarnavigation.component.html',
  styleUrls: ['./sidebarnavigation.component.css']
})
export class SidebarnavigationComponent implements OnInit {

  constructor(private globalServiceService: GlobalServiceService) { 
    this.globalServiceService.sidebar();
  }

  ngOnInit() {
    
  }

}
