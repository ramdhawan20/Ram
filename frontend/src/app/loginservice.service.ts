import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoginserviceService {

  constructor() { }
  uname:string;
  checkusernameandpassword(uname: string, pwd: string){
    if( uname = 'admin' , pwd == 'admin' ){
      localStorage.setItem('username','admin');
      return true;
    }
    else{
      return false;
    }

  }
}
