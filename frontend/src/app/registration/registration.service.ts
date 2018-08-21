import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';

import { HttpClient } from '@angular/common/http';
import { AuthenticationTokenService, Jwt } from '@app/core/authentication/authenticationtoken.service';
import { Logger } from '@app/core';

export interface Credentials {
  // Customize received credentials here
  registerCode: string;
  teamName: string;
  password: string;
}

export interface Error {
    error: string;
}

const log = new Logger('RegistrationService');

@Injectable()
export class RegistrationService {

  constructor(private http: HttpClient,
              private authenticationTokenService: AuthenticationTokenService) {
  }

  /**
   * Authenticates the user.
   * @param {LoginContext} context The login parameters.
   * @return {Observable<Credentials>} The user credentials.
   */
  register(registration: Credentials): Observable<Jwt | Error> {
    log.debug('Register: ', registration);
    return this.http.post<Jwt>('/api/register', registration)
    .pipe(
        jwt => {
            jwt.subscribe(d => this.authenticationTokenService.setCredentials(d));
            return of(jwt);
        },
        err => {
            return of({'error': 'LOL'});
        }
    );
  }
}
