import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
//import { Observable } from 'rxjs';
import { Http, Headers, Response } from '@angular/http';
import { map } from 'rxjs/operators';

@Injectable()
export class GlobalServiceService {

  url = 'http://10.191.234.10:9090';
  logindata;
  getUserIdprofile;
  constructor(private http: HttpClient) { }



  // loginservice(username, password) {

  //   this.logindata = JSON.stringify(
  //     {
  //       "userId": username,
  //       "password": password
  //     });

  //   return this.http.post(this.url + '/integration/services/restService/userLogin', this.logindata)

  // }

  loginservice(username, password) {

    this.logindata = JSON.stringify(
      {
        "userId": username,
        "password": password
      });

    return this.http.post('http://10.97.192.176:8080/login', this.logindata, {
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

}



