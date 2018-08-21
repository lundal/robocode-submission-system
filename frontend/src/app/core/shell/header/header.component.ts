import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationTokenService } from '../../authentication/authenticationtoken.service';
import { I18nService } from '../../i18n.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  menuHidden = true;

  constructor(private router: Router,
              private authenticationTokenService: AuthenticationTokenService,
              private i18nService: I18nService) { }

  ngOnInit() { }

  toggleMenu() {
    this.menuHidden = !this.menuHidden;
  }

  logout() {
    this.authenticationTokenService.logout()
      .subscribe(() => this.router.navigate(['/'], { replaceUrl: true }));
  }

  get username(): string | null {
    const subject = this.authenticationTokenService.subject;
    return subject ? subject : null;
  }

}
