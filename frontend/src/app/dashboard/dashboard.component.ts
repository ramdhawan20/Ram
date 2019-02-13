import { Component, OnInit } from '@angular/core';
import { ModalsService} from '../modal.service';
import {GlobalServiceService} from '../global-service.service'

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private modalService: ModalsService,private globalServiceService: GlobalServiceService){}
  //open popup code start
  openModal(id: string) {
    this.modalService.open(id);

}
//open popup code end

//close popup code start
closeModal(id: string) {
    this.modalService.close(id);
}
//close popup code end

columnDefs = [
  {headerName: 'Make', field: 'make'},
  {headerName: 'Model', field: 'model'},
  {headerName: 'Price', field: 'price', editable: true}
];

rowData = [];

ngOnInit() {

  this.globalServiceService.jsonCalling()
      .subscribe(result => {
        console.log(result);
      }, err => {
        console.log(err);
      })



  fetch('https://api.myjson.com/bins/15psn9')
    .then(result => result.json())
    .then(rowData => this.rowData = rowData);


    
     
    }




  }

