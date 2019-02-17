import {Component} from "@angular/core";
import {ICellRendererAngularComp} from "ag-grid-angular";

@Component({
    selector: 'child-cell',
    template: `<span><button style="height: 20px; font-size:12px;" (click)="invokeParentMethod()" class="btn btn-info">Edit</button></span>`,
    styles: [
        `.btn {
            line-height: 0.5
        }`
    ]
})
export class ChildMessageRenderer implements ICellRendererAngularComp {
    public params: any;

    agInit(params: any): void {
        this.params = params;
    }

    public invokeParentMethod() {
      alert(this.params.data.customer);
      //  this.params.context.componentParent.methodFromParent(`Row: ${this.params.node.rowIndex}, Col: ${this.params.colDef.headerName}`)
    }

    refresh(): boolean {
        return false;
    }
}