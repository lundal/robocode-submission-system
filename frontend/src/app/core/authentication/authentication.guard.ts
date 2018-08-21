import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';

import { Logger } from '../logger.service';
import { AuthenticationTokenService } from '@app/core/authentication/authenticationtoken.service';

const log = new Logger('AuthenticationGuard');

@Injectable()
export class AuthenticationGuard implements CanActivate {

  constructor(private router: Router,
              private authenticationTokenService: AuthenticationTokenService) { }

  canActivate(): boolean {
    if (this.authenticationTokenService.isAuthenticated()) {
      return true;
    }

    log.debug('Not authenticated, redirecting...');
    this.router.navigate(['/login'], { replaceUrl: true });
    return false;
  }

}
