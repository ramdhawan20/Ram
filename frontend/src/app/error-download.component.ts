import { Component, OnInit } from '@angular/core';
import { ICellRendererAngularComp } from "ag-grid-angular";
import { GlobalServiceService } from './global-service.service';

@Component({
    selector: 'app-error-download',
    template: `
  <!--<a href="JavaScript:void(0);" (click)="invokeParentMethod()" target="_self">{{this.params.data.errorLogFileName}}</a>-->
  <div *ngIf="isValid;else other_content">
    -
</div>

<ng-template #other_content><a href="JavaScript:void(0);" (click)="invokeParentMethod()" target="_self">{{this.params.data.errorLogFileName}}</a></ng-template>
      
  `,
    styles: [
        `.btn {
        line-height: 0.5
    }`
    ]
})
export class ErrorDownloadComponent implements ICellRendererAngularComp {

    public params: any;
    fileName;
    dwnfile;
    items;
    data;
    isValid: boolean;
    constructor(private globalServiceService: GlobalServiceService, ) { }
    agInit(params: any): void {
        this.params = params;
        if (this.params.data.errorLogFileName == "") {
            this.isValid = true;
        } else {
            this.isValid = false;
        }
    }

    public invokeParentMethod() {
        this.fileName = this.params.data.errorLogFileName;
        this.downloadToCsv();
    }

    refresh(): boolean {
        return false;
    }
    // Download Functionality

    downloadToCsv() {
        let url = this.globalServiceService.url + '/downloadFile/' + this.fileName;
        window.location.href = url;

    }

}

