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
  constructor(private http: HttpClient) { }

  loginservice(username, password) {

    this.logindata = JSON.stringify(
      {
        "userId": username,
        "password": password
      });

    return this.http.post(this.url+'/login', this.logindata, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }
sidebar(){
  jQuery(function ($) {
    $(".sidebar-dropdown > a").click(function() {
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

$("#close-sidebar").click(function() {
  $(".page-wrapper").removeClass("toggled");
});
$("#show-sidebar").click(function() {
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
  
    return this.http.get('/assets/Subscription.json', {
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
  
    return this.http.get('/assets/Subscription.json', {
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
			
    return this.http.post(this.url + '/uploadMigData', formData, {
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
  
    return this.http.get('/assets/usermanagement.json', {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })
    }).pipe(map((response: Response) => {
      console.log(response);
      return response;
    }));
  }




//addProduct 

addProduct(name,description,sku,startDate,endDate){
  this.newProductData = JSON.stringify(
    {
      "name":name,
      "description":description, 
      "sku":sku,
      "startDate":startDate,
      "endDate":endDate,
      
    });

  return this.http.post(this.url + '/newProduct', this.newProductData, {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    })
  }).pipe(map((response: Response) => {
    console.log(response);
    return response;
  }));
}
    //search Subcription
    searchSubcription(Subscription_id,customber_email,first_name,status,Created_on,Activated_on,Customber_name,plan_name,amount,last_bill,next){
      this.searchSubcriptionData = JSON.stringify(
        {
          "Subscription_id" : Subscription_id,
          "customber_email" : customber_email,
          "first_name"  : first_name,
          "status" : status,
          "Created_on" : Created_on,
          "Activated_on" : Activated_on,
          "Customber_name" : Customber_name,
          "plan_name" : plan_name,
          "amount": amount,
          "last_bill" :last_bill,
          "next" : next
        });
    
      return this.http.post(this.url + '/searchSubcription', this.searchSubcriptionData, {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
        })
      }).pipe(map((response: Response) => {
        console.log(response);
        return response;
      }));
    }

  //add user
  addUser(userId,profile,firstName,middleName,lastName,password){
    this.addUserData = JSON.stringify(
      {
        "userId":userId,  
        "userProfile":profile,       
        "userFirstName":firstName,
       "userMiddleName":middleName,
       "userLastName":lastName,
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
    editUser(userId,profile,firstName,middleName,lastName){
      this.editdata = JSON.stringify(
        {
          "userId":userId,  
          "userProfile":profile,       
          "userFirstName":firstName,
         "userMiddleName":middleName,
         "userLastName":lastName,
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
  activeDeactive(userId){
    this.activeDeactiveDta = JSON.stringify(
      {
        "userId":userId
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
    resetPwd(userId,newpwd){
      this.pwdResetData = JSON.stringify(
        {
          "userId":userId,
          "newAttribute":newpwd, 
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
     getUserData(){
     
      return this.http.get(this.url + '/users',{
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
        })
      }).pipe(map((response: Response) => {
        console.log(response);
        return response;
      }));
    }
    
     //search user data
     searchUserData(user_profile,user_name,first_name,status_val){
     
      this.searchData = JSON.stringify(
        {
          "userProfile":user_profile,
          "username":user_name, 
          "firstname":first_name,
          "status":status_val,
          
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


}



