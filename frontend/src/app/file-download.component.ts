import { Component, OnInit } from '@angular/core';
import {ICellRendererAngularComp} from "ag-grid-angular";

@Component({
  selector: 'app-file-download',
  template: `<a href="JavaScript:void(0);" style = "text-transform: capitalize; text-decoration:underline;" (click)="invokeParentMethod()">{{this.params.data.uploadFileName}}</a>`,
  styles: [
    `.btn {
        line-height: 0.5
    }`
]
})


  export class FileDownloadComponent implements ICellRendererAngularComp {
    public params: any;

    agInit(params: any): void {
        this.params = params;
    }

    public invokeParentMethod() {
        this.params.context.componentParent.methodFromParent(`Row: ${this.params.node.rowIndex}, Col: ${this.params.colDef.headerName}`)
    }

    refresh(): boolean {
        return false;
    }
}


