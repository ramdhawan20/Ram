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
  activeDeactiveDta;
  pwdResetData;
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


  //add user
  addUser(userId,profile,firstName,middleName,lastName){
    this.addUserData = JSON.stringify(
      {
        "userId":userId,  
        "userProfile":profile,       
        "userFirstName":firstName,
       "userMiddleName":middleName,
       "userLastName":lastName,
      });
  
    return this.http.post(this.url + '/addUser', this.addUserData, {
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
    
      return this.http.post(this.url + '/editUser', this.editdata, {
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
  
    return this.http.post(this.url + '/activateUser', this.activeDeactiveDta, {
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
    
      return this.http.post(this.url + '/reset', this.pwdResetData, {
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
     
      return this.http.get(this.url + '/getAllUsers',{
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
        })
      }).pipe(map((response: Response) => {
        console.log(response);
        return response;
      }));
    }

}



