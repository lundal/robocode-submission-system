import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { finalize } from 'rxjs/operators';

import { QuoteService } from './quote.service';
import { Logger } from '@app/core';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { AuthenticationTokenService } from '@app/core/authentication/authenticationtoken.service';

const log = new Logger('Registration');

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  isLoading: boolean;
  loggedIn: boolean;
  isDragging: boolean;
  deliveries: any[];

  constructor(private toastr: ToastrService,
              private http: HttpClient,
              private authenticationTokenService: AuthenticationTokenService) {
   }

  ngOnInit() {
    this.isLoading = true;
    this.loggedIn = this.authenticationTokenService.isAuthenticated();
    this.isDragging = false;
    this.getDeliveries();
  }

  getDeliveries() {
    if (!this.authenticationTokenService.isAuthenticated) {
      return;
    }

    this.http.get<any[]>('/api/delivery')
    .subscribe(success => {
      this.deliveries = success;
    });
  }

  public onDrop(event: DragEvent) {
    event.stopPropagation();
    event.preventDefault();
    this.isDragging = false;

    const formData = new FormData();
    const file = event.dataTransfer.files[0];
    formData.append('file', file);

    this.http.post('/api/delivery/upload', formData)
    .subscribe(
      success => {
        this.toastr.success('Uploaded ' + file.name, 'File upload');
        this.getDeliveries();
      },
      err => {
        if (err.status === 400) {
          console.log('Err', err);
          this.toastr.error(err.error.error, 'File upload error');
        } else {
          this.toastr.error('Unknown error, HTTP ' + err.status, 'File upload error');
        }
      }
    );
  }

  public dragEnter(event: DragEvent) {
    this.isDragging = true;
  }

  public onDragOver(event: DragEvent) {
    event.stopPropagation();
    event.preventDefault();
  }

  public dragLeave(event: DragEvent) {
    this.isDragging = false;
  }
}
