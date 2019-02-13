import { Component, OnInit } from '@angular/core';
import { GlobalServiceService } from '../global-service.service'
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
 registerForm: FormGroup;
    submitted = false;
    isDisabled: boolean = true;
    validEmail:boolean = false;
  constructor(private globalServiceService: GlobalServiceService, private formBuilder: FormBuilder) { }
  
  ngOnInit() {
     this.registerForm = this.formBuilder.group({
            // firstName: ['', Validators.required],
            // lastName: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(0)]]
        });
        

  }
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
  this.globalServiceService.loginservice(emailvalue, pwdvalue)
  .subscribe(result => {
    console.log(result);
  }, err => {
    console.log(err);
  }
  );
}
}
  

