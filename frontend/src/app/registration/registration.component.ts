import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs/operators';

import { environment } from '@env/environment';
import { Logger, I18nService } from '@app/core';
import { AuthenticationTokenService, Jwt } from '@app/core/authentication/authenticationtoken.service';
import { HttpClient } from '@angular/common/http';

const log = new Logger('Registration');

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  version: string = environment.version;
  error: string;
  registrationForm: FormGroup;
  isLoading = false;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private i18nService: I18nService,
              private http: HttpClient,
              private authenticationTokenService: AuthenticationTokenService) {
    if (this.authenticationTokenService.isAuthenticated()) {
      router.navigateByUrl('/');
    }
    this.createForm();
  }

  ngOnInit() { }

  register() {
    this.isLoading = true;
    this.error = '';
    return this.http.post<Jwt>('/api/auth/registerTeam', this.registrationForm.value)
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
    this.registrationForm = this.formBuilder.group({
      registrationCode: ['', Validators.required],
      teamName: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

}
