import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {HeaderComponent} from './header/header.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {ContactListComponent} from './contact-list/contact-list.component';
import{UsermanagementComponent} from './usermanagement/usermanagement.component';
import{TransactionsComponent} from './transactions/transactions.component';
import{PlanComponent} from './plan/plan.component';
import { AuthGuard } from './auth.guard';
import { ImportPlanComponent } from './import-plan/import-plan.component';
import { ProductComponent } from './products/product.component';
import { SubscriptionreportComponent } from './subscriptionreport/subscriptionreport.component';


const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'header', component: HeaderComponent, canActivate :[AuthGuard] },
  { path: 'product', component: ProductComponent,
  children: [
    {
      path: 'import',
      component: ImportPlanComponent
    }],
  
  canActivate :[AuthGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate :[AuthGuard] },
  { path: 'subscriptions', component: ContactListComponent,
  children: [
    {
      path: 'report',
      component: SubscriptionreportComponent
    }],
  canActivate :[AuthGuard] },
  { path: 'usermanagement', component: UsermanagementComponent, canActivate :[AuthGuard] },
  { path: 'transactions', component: TransactionsComponent, canActivate :[AuthGuard] },
  { path: 'plan', component: PlanComponent, canActivate :[AuthGuard] },
  { path: '**', redirectTo: '' },
  { path: '', redirectTo: ' ', pathMatch: 'full' },

];

@NgModule({
  imports: [RouterModule.forRoot(routes),RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
