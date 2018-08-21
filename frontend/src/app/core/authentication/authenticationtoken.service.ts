import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';

const credentialsKey = 'credentials';


export interface Jwt {
  jwt: string;
}

/**
 * Provides a base for authentication workflow.
 * The Credentials interface as well as login/logout methods should be replaced with proper implementation.
 */
@Injectable()
export class AuthenticationTokenService {

  private _credentials: Jwt | null;
  private _subject: string | null;

  constructor() {
    const savedCredentials = sessionStorage.getItem(credentialsKey) || localStorage.getItem(credentialsKey);
    if (savedCredentials) {
      this._credentials = JSON.parse(savedCredentials);
      this._subject = JSON.parse(atob(this._credentials.jwt.split('.')[1])).sub;
    }
  }

  /**
   * Logs out the user and clear credentials.
   * @return {Observable<boolean>} True if the user was logged out successfully.
   */
  logout(): Observable<boolean> {
    // Customize credentials invalidation here
    this.setCredentials();
    return of(true);
  }

  /**
   * Checks is the user is authenticated.
   * @return {boolean} True if the user is authenticated.
   */
  isAuthenticated(): boolean {
    return !!this.credentials;
  }

  /**
   * Gets the user credentials.
   * @return {Credentials} The user credentials or null if the user is not authenticated.
   */
  get credentials(): Jwt | null {
    return this._credentials;
  }

  get subject(): string | null {
    return this._subject;
  }

  /**
   * Sets the user credentials.
   * The credentials may be persisted across sessions by setting the `remember` parameter to true.
   * Otherwise, the credentials are only persisted for the current session.
   * @param {Credentials=} credentials The user credentials.
   * @param {boolean=} remember True to remember credentials across sessions.
   */
  setCredentials(credentials?: Jwt) {
    this._credentials = credentials || null;

    if (credentials) {
      localStorage.setItem(credentialsKey, JSON.stringify(credentials));
      this._subject = JSON.parse(atob(this._credentials.jwt.split('.')[1])).sub;
    } else {
      localStorage.removeItem(credentialsKey);
      this._subject = null;
    }
  }
}
