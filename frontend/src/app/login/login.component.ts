import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs/operators';

import { environment } from '@env/environment';
import { Logger, I18nService } from '@app/core';
import { AuthenticationTokenService, Jwt } from '@app/core/authentication/authenticationtoken.service';
import { HttpClient } from '@angular/common/http';

const log = new Logger('Login');

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  version: string = environment.version;
  error: string;
  loginForm: FormGroup;
  isLoading = false;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private http: HttpClient,
              private authenticationTokenService: AuthenticationTokenService) {
    if (this.authenticationTokenService.isAuthenticated()) {
      router.navigateByUrl('/');
    }
    this.createForm();
  }

  ngOnInit() { }

  login() {
    this.isLoading = true;
    this.error = '';
    return this.http.post<Jwt>('/api/auth/login', this.loginForm.value)
    .subscribe(
      jwt => {
        this.authenticationTokenService.setCredentials(jwt);
        this.router.navigateByUrl('/');
        this.isLoading = false;
      },
      err => {
        this.error = err.error.error;
        this.isLoading = false;
      }
    );
  }

  private createForm() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

}
