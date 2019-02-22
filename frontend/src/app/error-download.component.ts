import { Component, OnInit } from '@angular/core';
import {ICellRendererAngularComp} from "ag-grid-angular";

@Component({
  selector: 'app-error-download',
  template: `<a href="JavaScript:void(0);" (click)="invokeParentMethod()">{{this.params.data.errorLogFileName}}</a>`,
  styles: [
    `.btn {
        line-height: 0.5
    }`
]
})
export class ErrorDownloadComponent implements ICellRendererAngularComp {



  public params: any;
  data;

  agInit(params: any): void {
      this.params = params;
      
  }

  constructor() { }

  ngOnInit() {
  }

    public invokeParentMethod() {
        console.log("invoke method");
        
       
   
            // this.allApicallingService.searchUseruser_accountDownload(this.newdropdownID, Username, firstName, this.statusvalue).subscribe(
            //     result => {
            //        
            //         this.items = JSON.parse(JSON.stringify(result.userDetailsResponseDTOList).replace(/\s(?=\w+":)/g, ""));
            //         this.data = this.items;
      
         
            //         this.download();
            //     },
            //     err => {
            //         this.spinnerService.hide();
            //         document.getElementById("errofactive").style.display = "block";
            //         setTimeout(this.myFunctionreset, 3000);
            //     });
        }
        download() {
            var csvData = this.ConvertToCSV(this.data);
            var a = document.createElement("a");
            a.setAttribute('style', 'display:none;');
            document.body.appendChild(a);
            var blob = new Blob([csvData], {
                type: 'text/csv'
            });
            var url = window.URL.createObjectURL(blob);
            a.href = url;
            a.download = 'Useraccount.csv';
            a.click();
        }
        ConvertToCSV(objArray) {
            var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
            var str = '';
            var row = "";
      
            for (var index in objArray[0]) {
      
                row += index + ',';
            }
            row = row.slice(0, -1);
      
            str += row + '\r\n';
      
            for (var i = 0; i < array.length; i++) {
                var line = '';
                for (var index in array[i]) {
                    if (line != '') line += ','
      
                    line += array[i][index];
                }
                str += line + '\r\n';
            }
            return str;
        }
    refresh(): boolean {
        return false;
    }
}
