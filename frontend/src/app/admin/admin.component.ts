import { Component, OnInit, ViewEncapsulation, OnDestroy } from '@angular/core';
import { finalize } from 'rxjs/operators';

import { Logger } from '@app/core';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { AuthenticationTokenService } from '@app/core/authentication/authenticationtoken.service';
import { ActivatedRoute } from '@angular/router';
import { saveAs } from 'file-saver';
import { timer } from 'rxjs/observable/timer';

const log = new Logger('Admin');

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss'],
})
export class AdminComponent implements OnInit, OnDestroy {
  location: string;
  private sub: any;
  teams: any[] = [];
  locationInfo: any = {};
  tidIgjen = '';
  private tidSub: any;
  private teamSub: any;

  constructor(private route: ActivatedRoute,
              private http: HttpClient,
              private authenticationTokenService: AuthenticationTokenService) {
   }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.location = params['location'];
      this.teams = [];
      this.getTeams();
      this.getLocationInfo();
      this.teamSub = timer(5000, 5000);
      this.teamSub.subscribe(() => {
        this.getTeams();
      });
   });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
    this.tidSub.unsubscribe();
    this.teamSub.unsubscribe();
  }

  getTeams() {
    this.http.get<any[]>(`/api/team/${this.location}`)
    .subscribe(success => {
      this.teams = success;
    });
  }

  private formatNumber(num: Number) {
    if (num < 10) {
      return '0' + num.toString();
    }
    return num.toString();
  }

  getLocationInfo() {
    this.http.get<any[]>(`/api/location/${this.location}`)
    .subscribe(success => {
      this.locationInfo = success;
      this.tidSub = timer(500, 500);
      this.tidSub.subscribe(() => {
        const now = Date.now();
        const then = this.locationInfo.deadline;
        if ((then - now) < 0) {
          this.tidIgjen = 'UTLØPT!';
          this.tidSub.unsubscribe();
        }
        let seconds = Math.floor((then - now) / 1000);
        const hours = Math.floor(seconds / 3600);
        seconds -= hours * 3600;
        const minutes = Math.floor(seconds / 60);
        seconds -= minutes * 60;
        this.tidIgjen = this.formatNumber(hours) + ':' + this.formatNumber(minutes) + ':' + this.formatNumber(seconds);
      });
    });
  }

  download() {
    this.http.get(`/api/download/${this.location}`, {responseType: 'blob'})
    .subscribe(success => {
      saveAs(success, `robots-${this.location}.zip`);
    });
  }
}
