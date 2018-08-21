import { Component, OnInit } from '@angular/core';

import { environment } from '@env/environment';

@Component({
  selector: 'app-om-oss',
  templateUrl: './om-oss.component.html',
  styleUrls: ['./om-oss.component.scss']
})
export class OmOssComponent implements OnInit {

  version: string = environment.version;

  constructor() { }

  ngOnInit() { }

}
