import { Component, OnInit } from '@angular/core';
import{Router} from '@angular/router'
import { GlobalServiceService } from '../global-service.service'
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginserviceService } from '../loginservice.service';
import { FlashMessagesService } from 'angular2-flash-messages';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers:[LoginserviceService]
})
export class LoginComponent implements OnInit {
 registerForm: FormGroup;
    submitted = false;
    isDisabled: boolean = true;
    validEmail:boolean = false;
    msg;
  constructor(private globalServiceService: GlobalServiceService, private flashMessage: FlashMessagesService, private loginserviceService: LoginserviceService, private formBuilder: FormBuilder,  private router: Router) { }
  
  ngOnInit() {
   
     this.registerForm = this.formBuilder.group({
            // firstName: ['', Validators.required],
            // lastName: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(0)]]
        });
        

  }
  // check(uname: string, p: string)
  // {
  //   var output = this.loginserviceService.checkusernameandpassword(uname,p);
  //   if(output == true)
  //   {
  //     this.routes.navigate(['/dashboard']);
  //   }
  //   else{
  //     this.msg = 'Invalid username or password';
  //   }
  // }
  get f() { return this.registerForm.controls; }
  onSubmit() {
    this.submitted = true;
    if (this.registerForm.invalid) {
        return;
    }

    // alert('SUCCESS!! :-)\n\n' + JSON.stringify(this.registerForm.value))
    
}
// onChange(newValue) {
//   const validEmailRegEx = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
//   if (validEmailRegEx.test(newValue)) {
//       this.validEmail = true;
//   }else {
//     this.validEmail = false;
//   }

// }
  loginvalidation(emailvalue, pwdvalue) {
	  if(emailvalue=="admin" && pwdvalue=="admin"){
		  this.router.navigate(['/dashboard']);
	  }else{
		  
  this.globalServiceService.loginservice(emailvalue, pwdvalue)
  .subscribe(result => {
    console.log(result);
    this.router.navigate(['/dashboard']);
  }, err => {
    console.log(err);
    let msg=err.error.error;
      this.flashMessage.show(msg, { cssClass: 'alert-danger', timeout: 10000 });
    }
  );
	  }
}
}
  

