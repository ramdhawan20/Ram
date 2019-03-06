import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Http, Headers, Response } from '@angular/http';
import { map } from 'rxjs/operators';

@Injectable()
export class GlobalServiceService {

  url = 'http://localhost:8080';
  logindata;
  editdata;
  getUserIdprofile;
  addUserData;
  searchSubcriptionData;
  activeDeactiveDta;
  pwdResetData;
  searchData;
  newProductData;
  subscriptionData;
  constructor(private http: HttpClient) { }

  loginservice(username, password) {

    this.logindata = JSON.stringify(
      {
        "userId": username,
        "password": password
      });

    return this.http.post(this.url + '/login', this.logindata, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }
  sidebar() {
    jQuery(function ($) {
      $(".sidebar-dropdown > a").click(function () {
        $(".sidebar-submenu").slideUp(200);
        if (
          $(this)
            .parent()
            .hasClass("active")
        ) {
          $(".sidebar-dropdown").removeClass("active");
          $(this)
            .parent()
            .removeClass("active");
        } else {
          $(".sidebar-dropdown").removeClass("active");
          $(this)
            .next(".sidebar-submenu")
            .slideDown(200);
          $(this)
            .parent()
            .addClass("active");
        }
      });

      $("#close-sidebar").click(function () {
        $(".page-wrapper").removeClass("toggled");
      });
      $("#show-sidebar").click(function () {
        $(".page-wrapper").addClass("toggled");
      });
    });
  }
  jsonCalling() {

    return this.http.get('/assets/dummy.json', {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }


  SubscriptionCalling() {
    this.subscriptionData = JSON.stringify({
      // "subscriptionId": "",
      // "customerName": "",
      // "email": "",
      // "planName": "",
      // "status": "",
      // "price": "",
      // "createdDate": "",
      // "activatedDate": "",
      // "lastBillDate": "",
      // "nextBillDate": ""
    })
    
    return this.http.put(this.url + '/subscriptions', this.subscriptionData, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }

  //SubscriptionReportCalling
  SubscriptionreportCalling() {
    this.subscriptionData = JSON.stringify({
      "subscriptionId": "",
      "customerName": "",
      "email": "",
      "planName": "",
      "status": "",
      "price": "",
      "createdDate": "",
      "activatedDate": "",
      "lastBillDate": "",
      "nextBillDate": ""
    })
    return this.http.put(this.url + '/subscriptions', this.subscriptionData, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }

  //upload case
  uploadExpData(formData) {

    return this.http.post(this.url + '/uploadProductData', formData, {
      headers: new HttpHeaders({
        'Content-Type': 'multipart/form-data',
        'Accept': 'application/json',

      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }


  usermanagementCalling() {

    return this.http.get(this.url + '/getProducts', {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }




  //addProduct 

  addProduct(name, description, sku, startDate, endDate, pCode) {
    this.newProductData = JSON.stringify(
      {


        "productDescription": description,
        "productDispName": name,
        "productExpDate": endDate,
        "productStartDate": startDate,
        "productTypeCode": pCode,
        "sku": sku,



      });

    return this.http.post(this.url + '/product', this.newProductData, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }
  //search Subcription
  searchSubcription(subscriptionNo, customerName, email, planName, status, price, createdDate, activatedDate, lastBillDate, nextBillDate) {
    this.searchSubcriptionData = JSON.stringify(
      {
        "subscriptionId": subscriptionNo,
        "customerName": customerName,
        "email": email,
        "planName": planName,
        "status": status,
        "price": price,
        "createdDate": createdDate,
        "activatedDate": activatedDate,
        "lastBillDate": lastBillDate,
        "nextBillDate": nextBillDate
      });

    return this.http.put(this.url + '/subscriptions', this.searchSubcriptionData, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }

  //add user
  addUser(userId, profile, firstName, middleName, lastName, password) {
    this.addUserData = JSON.stringify(
      {
        "userId": userId,
        "userProfile": profile,
        "userFirstName": firstName,
        "userMiddleName": middleName,
        "userLastName": lastName,
        "attribute": password
      });

    return this.http.post(this.url + '/user', this.addUserData, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }

  //edit user
  editUser(userId, profile, firstName, middleName, lastName) {
    this.editdata = JSON.stringify(
      {
        "userId": userId,
        "userProfile": profile,
        "userFirstName": firstName,
        "userMiddleName": middleName,
        "userLastName": lastName,
      });

    return this.http.put(this.url + '/user', this.editdata, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));

  }

  //active-deactive user
  activeDeactive(userId) {
    this.activeDeactiveDta = JSON.stringify(
      {
        "userId": userId
      });

    return this.http.post(this.url + '/activate', this.activeDeactiveDta, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }

  //pwd reset user
  resetPwd(userId, newpwd) {
    this.pwdResetData = JSON.stringify(
      {
        "userId": userId,
        "newAttribute": newpwd,
      });

    return this.http.put(this.url + '/reset', this.pwdResetData, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }

  //All user data
  getUserData() {

    return this.http.get(this.url + '/users', {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }

  //search user data
  searchUserData(user_profile, user_name, first_name, status_val) {

    this.searchData = JSON.stringify(
      {
        "userProfile": user_profile,
        "username": user_name,
        "firstname": first_name,
        "status": status_val,

      });

    return this.http.post(this.url + '/search', this.searchData, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }
  fetchdropdownvalues(){
  
    return this.http.get(this.url + '/getProductType', {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }


}



