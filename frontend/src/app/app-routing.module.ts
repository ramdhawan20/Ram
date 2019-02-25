import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {HeaderComponent} from './header/header.component';
import {TabComponent} from './tab/tab.component';
import {ProductsComponent} from './products/products.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {ContactListComponent} from './contact-list/contact-list.component';
import{UsermanagementComponent} from './usermanagement/usermanagement.component';
import{TransactionsComponent} from './transactions/transactions.component';
import{PlanComponent} from './plan/plan.component';
import { AuthGuard } from './auth.guard';
const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'header', component: HeaderComponent, canActivate :[AuthGuard] },
  { path: 'tab', component: TabComponent, canActivate :[AuthGuard] },
  { path: 'product', component: ProductsComponent, canActivate :[AuthGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate :[AuthGuard] },
  { path: 'subscriptions', component: ContactListComponent, canActivate :[AuthGuard] },
  { path: 'usermanagement', component: UsermanagementComponent, canActivate :[AuthGuard] },
  { path: 'transactions', component: TransactionsComponent, canActivate :[AuthGuard] },
  { path: 'plan', component: PlanComponent, canActivate :[AuthGuard] },
  { path: '**', redirectTo: '' },
  { path: '', redirectTo: ' ', pathMatch: 'full' },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
