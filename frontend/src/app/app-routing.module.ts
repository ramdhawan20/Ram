import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {HeaderComponent} from './header/header.component';
import {TabComponent} from './tab/tab.component';
import {ProductsComponent} from './products/products.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {ContactListComponent} from './contact-list/contact-list.component';
import{UsermanagementComponent} from './usermanagement/usermanagement.component';
const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'header', component: HeaderComponent },
  { path: 'tab', component: TabComponent },
  { path: 'product', component: ProductsComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'subscriptions', component: ContactListComponent },
  { path: 'usermanagement', component: UsermanagementComponent },
  { path: '**', redirectTo: '' },
  { path: '', redirectTo: ' ', pathMatch: 'full' },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
