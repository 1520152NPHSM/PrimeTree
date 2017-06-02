import { Component, OnInit} from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { FormElementsService } from '../formElements.service';
import { DomSanitizer, SafeUrl, SafeStyle } from '@angular/platform-browser';

@Component({
  selector: 'input-image',
  templateUrl: './image.component.html',
  styleUrls: [ './image.component.css' ]
})
export class ImageFormComponent implements OnInit {
  model : any;
  form : FormGroup;
  data : any;
  imagesrc : SafeUrl = '';
  image : SafeStyle;

  private div : Element;

  private addMulipleEventListener(element : Element, eventstring : string, handle : any) : void {
    let events : string[] = eventstring.split(' ');
    events.forEach((event : string) => {
      element.addEventListener(event, handle);
    });
  }

  private handleEvents() : void {
    this.div = document.querySelector('.image-box');

    if (!this.div) {
      return;
    }
    this.addMulipleEventListener(this.div, 'drag', (event : any) => {
      event.preventDefault();
      event.stopPropagation();
    });

    this.addMulipleEventListener(this.div, 'dragover dragenter', (event : any) => {
      event.preventDefault();
      event.stopPropagation();
      this.div.classList.add('is-dragover');
    });

    this.addMulipleEventListener(this.div, 'dragleave dragend drop', (event : any) => {
      event.preventDefault();
      event.stopPropagation();
      this.div.classList.remove('is-dragover');
    });

    /**User drops a file into the dropper*/
    this.addMulipleEventListener(this.div, 'drop', (event : any) => {
      console.log("DATA TRANSFER", event.dataTransfer);
      this.preloadImage(event.dataTransfer.files[0]);




      // this.fileToBase(this.data.imageAsFile, (base : string) => {
      //   this.data.imageAsBase = base;
      //   this.data.imageAsByteArray = this.baseToByte(base);
      //   let file : File = this.byteToFile(this.data.imageAsByteArray);
      //   // let str : StreamReader = new StreamReader();
      //   this.imagesrc = 'data:image/jpeg;base64,' + this.data.imageAsByteArray;
      //
      // });
      // this.fileToByteArray(this.data.imageAsFile, (bytearray : Uint8Array) => {
      //   // let src : string = String.fromCharCode.apply(null, bytearray);
      //   this.data.imageAsByteArray = bytearray;
      //   let ele = document.querySelector('#file-input-image');
      //   ele.appendChild(this.decodeArrayBuffer(bytearray, () => {
      //
      //   }));
      // });
    });

    /**User clicks on the file-upload*/
    // this.addMulipleEventListener(this.div, 'click', (event : any) => {
    //   console.log("DATA TRANSFER", event);
    //   this.preloadImage(event.dataTransfer.files[0]);
    // });
  };

  /**Generates and presents an image file from the uploader*/
  preloadImage(imageFile : File) : void {

    /*GENERATE IMAGE PREVIEW*/
    let imageResult = new Image();
    let ImageComponent : ImageFormComponent = this;
    imageResult.onload = function() {

      /*Hide the image upload*/
      let imageInputContainer = document.querySelector(".image-input-container");
      imageInputContainer.classList.remove("active");

      /*Show the image preview - HAVE TO DO THIS FIRST TO GRAB CONTAINER DIMENSIONS*/
      let resultImageContainer = document.querySelector(".result-image-container");
      resultImageContainer.classList.add("active");

      //Workaround to calling "this" within a callback
      ImageComponent.setImageContainerDimensions();

      /*Inject image into preview as a background image and center it*/
      let imagePreviewContainer = <HTMLElement>document.querySelector("#file-input-image");
      imagePreviewContainer.style.backgroundImage = "url('"+imageResult.src+"')";
      imagePreviewContainer.style.backgroundPosition = "50% 50%";

      /*Get the image's as well as its container's dimensions
      to calculate the perfect initial display*/
      let imgWidth = imageResult.width;
      let imgHeight = imageResult.height;
      let ipcWidth = imagePreviewContainer.clientWidth;
      let ipcHeight = imagePreviewContainer.clientHeight;


      /*Get the image's orientation*/
      let isLandscape = false;
      if(imgWidth > imgHeight) {//If it's not landscape it's either portrait or square
        isLandscape = true;
      }

      /**Calculate and set initial display size*/
      let imgRatio;
      let zoomRange = <HTMLInputElement>document.querySelector("#zoom-range");
      if(isLandscape) {
        imgRatio = imgHeight / imgWidth;
        if(ipcWidth * imgRatio < ipcHeight) {
          imagePreviewContainer.style.backgroundSize = "auto " + ipcHeight + "px";
          zoomRange.min = ipcHeight + "";
          zoomRange.max = imgHeight + "";
          zoomRange.value = imgHeight + "";
        } else {
          imagePreviewContainer.style.backgroundSize = "auto " + (ipcWidth * imgRatio) + "px";
          zoomRange.min = ipcWidth * imgRatio + "";
          zoomRange.max = imgWidth + "";
          zoomRange.value = ipcWidth * imgRatio + "";
        }
      } else { //Pretty much let the user do what they like
        imagePreviewContainer.style.backgroundSize = "auto " + ipcHeight + "px";
        zoomRange.min = "0";
        zoomRange.max = imgHeight + "";
        zoomRange.value = ipcHeight + "";
      }
    }
    imageResult.src = URL.createObjectURL(imageFile);
  }

  zoomImage() : void {
    let rangeSlider = <HTMLInputElement>document.querySelector("#zoom-range");
    let imageContainer = <HTMLElement>document.querySelector("#file-input-image");
    //Adapt zoom-level to image-container background-image
    imageContainer.style.backgroundSize = "auto " + rangeSlider.value + "px";
  }

  moveImage(direction : String) : void {
    let imagePreviewContainer = <HTMLElement>document.querySelector("#file-input-image");
    let currentXpos = parseFloat(imagePreviewContainer.style.backgroundPositionX);
    let currentYpos = parseFloat(imagePreviewContainer.style.backgroundPositionY);
    const moveBy = 5;
    if(direction === 'up') {
      imagePreviewContainer.style.backgroundPositionY = currentYpos + moveBy + "%";
    } else if (direction === 'down') {
      imagePreviewContainer.style.backgroundPositionY = currentYpos - moveBy + "%";
    } else if (direction === 'left') {
      imagePreviewContainer.style.backgroundPositionX = currentXpos - moveBy + "%";
    } else if (direction === 'right') {
      imagePreviewContainer.style.backgroundPositionX = currentXpos + moveBy + "%";
    }
  }

  removeImage() : void {
    const imagePreviewContainer = document.querySelector(".result-image-container");
    const imagePreview = document.querySelector("#file-input-image");
    //Reset the image
    imagePreview.innerHTML="";
    //Hide the image preview
    imagePreviewContainer.classList.remove("active");
    //Show the image dropper
    document.querySelector(".image-input-container").classList.add("active");
  }

  /**Sets the image container to the OpenGraph dimension of 1:0.525*/
  private setImageContainerDimensions() : void {
    let resultImageContainer = <HTMLElement>document.querySelector(".result-image-container");
    resultImageContainer.style.height = resultImageContainer.clientWidth * 0.525 + "px";

  }

  input(event : any) {
    this.data.imageAsFile = event.target.files[0];
    this.preloadImage(event.target.files[0]);
    let path : string = URL.createObjectURL(this.data.imageAsFile);
    let reader : FileReader = new FileReader();
    reader.onloadend = () => {
      this.image = this.domSanitizer.bypassSecurityTrustStyle('url(' + reader.result + ')');
    }
    reader.readAsDataURL(this.data.imageAsFile);
    console.log(this.data.imageAsFile, path);
    // this.image = this.domSanitizer.bypassSecurityTrustStyle('url(' + path + ')');
  }

  // http://stackoverflow.com/questions/4564119/javascript-convert-byte-to-image
  decodeArrayBuffer(buffer : any, onLoad : any) : HTMLImageElement {
    var mime;
    // var a = new Uint8Array(buffer);
    var a = buffer;
    var nb = a.length;
    if (nb < 4)
        return null;
    var b0 = a[0];
    var b1 = a[1];
    var b2 = a[2];
    var b3 = a[3];
    if (b0 == 0x89 && b1 == 0x50 && b2 == 0x4E && b3 == 0x47)
        mime = 'image/png';
    else if (b0 == 0xff && b1 == 0xd8)
        mime = 'image/jpeg';
    else if (b0 == 0x47 && b1 == 0x49 && b2 == 0x46)
        mime = 'image/gif';
    else
        return null;
    var binary = "";
    for (var i = 0; i < nb; i++)
        binary += String.fromCharCode(a[i]);
    var base64 = window.btoa(binary);
    var image = new Image();
    image.onload = onLoad;
    image.src = 'data:' + mime + ';base64,' + base64;

    return image;
}


  constructor(
    private formElementsService : FormElementsService,
    private domSanitizer : DomSanitizer
  ) {
    this.model = this.formElementsService.model;
    this.form = this.formElementsService.form;
    this.data = this.formElementsService.data;
    if (typeof this.model.image === 'undefined') {
      this.model.image = null;
    }
    if (typeof this.model.imageAsBase === 'undefined') {
      this.model.imageAsByteArray = null;
    }
    if (typeof this.data.imageAsFile === 'undefined') {
      this.data.imageAsFile = null;
    }
  }

  submitImage(event : any) : void {
    if (event === null || event.target === null || event.target.files === null) {
      return;
    }
    let file = event.target.files[0];
    let reader : FileReader = new FileReader();
    // reader.onload = this.readerOnload.bind(this);
    reader.readAsDataURL(file);
    reader.onloadend = () => {
      this.model.imageAsByteArray = this.baseToByte(reader.result);
    }
    // this.model.imageObj = file;
  }

  private fileToBase(file : File, callback : any) : void {
    let reader : FileReader = new FileReader();
    // file.
    // console.log(file);

    reader.readAsDataURL(file);
    reader.onloadend = () => {
      callback(reader.result);
    }
  }

  private fileToByteArray(file : File, callback: any) : void {
    let reader : FileReader = new FileReader();
    reader.readAsArrayBuffer(file);
    reader.onload = () => {
      callback(new Uint8Array(reader.result));
    }
  }

  private readerOnload(e : any) {
    let reader = e.target;
    this.imagesrc = reader.result;
    this.model.image = this.imagesrc;
  }

  private baseToByte(base : string) : Uint8Array {
    // base = base.replace(/^data:image\/(png|jpg|jpeg);base64/‌​, '');
    // base = base.replace('data:image/','');
    // base = base.replace(/png|jpg|jpeg/, '');
    // base = base.replace(';base64,', '');
    base = base.replace(/^data:image\/(jpg|png|jpeg);base64,/, '');
    let byteCharacters = window.atob(base);
    let length = byteCharacters.length;
    let byteArray : Uint8Array = new Uint8Array(new ArrayBuffer(length));
    for (let i = 0; i < length; i++) {
      byteArray[i] = byteCharacters.charCodeAt(i);
    }
    return byteArray;
  }

  ngOnInit() : void {
    this.handleEvents();
  }

  private byteToFile(byteArray : Uint8Array[]) : File {
    let file : File = new File(byteArray, 'abcdef' + '.jpg', {
      type: 'image/jepg'
    });
    return file;
  }

  private byteToBase(byteArray : Uint8Array) : string {
    let base : string = 'data:image/jpeg;base64,';
    let binary : string = '';
    const length : number = byteArray.length;
    for (let i = 0; i < length; i++) {
      binary += String.fromCharCode(byteArray[i]);
    }
    return base + window.btoa(binary);
  }
}
