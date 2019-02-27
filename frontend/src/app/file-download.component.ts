import { Component, OnInit } from '@angular/core';
import { ICellRendererAngularComp } from "ag-grid-angular";
import { GlobalServiceService } from './global-service.service';

@Component({
    selector: 'app-file-download',
    template: `<a href="JavaScript:void(0);" (click)="invokeParentMethod()" target="_self" >{{this.params.data.uploadFileName}}</a>`,
    styles: [
        `.btn {
        line-height: 0.5
    }`
    ]
})

export class FileDownloadComponent implements ICellRendererAngularComp {
    public params: any;
    fileName;
    dwnfile;
    items;
    data;
    constructor(private globalServiceService: GlobalServiceService, ) { }
    agInit(params: any): void {
        this.params = params;
    }

    public invokeParentMethod() {
        this.fileName = this.params.data.uploadFileName;
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
