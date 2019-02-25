import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivate, Router } from '@angular/router';
import { Observable, observable } from 'rxjs';

@Injectable()

export class AuthGuard implements CanActivate{

  constructor( private routes : Router){}
  
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean{
      if(localStorage.getItem('username')!= null){
        return true;
      }
      else{
        this.routes.navigate(['/login']);
        return false;
      }
    }

}





// @Injectable({
//   providedIn: 'root'
// })

// export class AuthGuard implements  {
  
// }
