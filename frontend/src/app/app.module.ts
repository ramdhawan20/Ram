import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProductsComponent } from './products/products.component';
import { HeaderComponent } from './header/header.component';
import { TabComponent } from './tab/tab.component';
import { NgbModule,NgbPaginationModule, NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { ContactListComponent } from './contact-list/contact-list.component';
import { GlobalServiceService} from './global-service.service'
import {HttpClientModule} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import {HttpModule} from '@angular/http';
import { AgGridModule } from 'ag-grid-angular';
import { ModalsService } from './modal.service';
import{ModalPopUpComponent} from './modal.component'; 
import { GridModule } from '@syncfusion/ej2-angular-grids';
import { PageService, SortService, FilterService, GroupService } from '@syncfusion/ej2-angular-grids';
import { TransactionsComponent } from './transactions/transactions.component';
import { ChildMessageRenderer } from "./child-message-renderer.component";
import { UsermanagementComponent } from './usermanagement/usermanagement.component';
import { FlashMessagesModule } from 'angular2-flash-messages';
import { ErrorDownloadComponent } from "./error-download.component";
import { FileDownloadComponent } from './file-download.component';
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    ProductsComponent,
    HeaderComponent,
    TabComponent,
    ContactListComponent,
    ModalPopUpComponent,
    TransactionsComponent,
    ChildMessageRenderer,
    UsermanagementComponent,
    ErrorDownloadComponent,
    FileDownloadComponent
  ],

  imports: [
    BrowserModule,FormsModule,HttpClientModule,HttpModule,GridModule,ReactiveFormsModule,
    AppRoutingModule,NgbModule.forRoot(),FlashMessagesModule.forRoot(),  
    AgGridModule.withComponents([ChildMessageRenderer]),NgbPaginationModule, NgbAlertModule,AgGridModule.withComponents([ErrorDownloadComponent])  ,
    AgGridModule.withComponents([FileDownloadComponent])  
  ],
 
  providers: [GlobalServiceService, ModalsService, PageService, SortService, FilterService, GroupService,ChildMessageRenderer],
  bootstrap: [AppComponent]
})
export class AppModule { }
