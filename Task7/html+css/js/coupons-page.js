var coupons = [
  {
    "tags": [
      { "name": "water" }
    ],
    "picture": "../img/beach.jpg",
    "name": "Water chill",
    "description": "beach relax",
    "price": "235",
    "duration": "2 days"
  },
  {
    "tags": [
      { "name": "excursion" },
      { "name": "photos" }
    ],
    "picture": "../img/ride.jpg",
    "name": "Ride to the city",
    "description": "go to LA",
    "price": "235",
    "duration": "2 days"
  },
  {
    "tags": [
      { "name": "excursion" },
      { "name": "photos" }
    ],
    "picture": "../img/cheetah.jpg",
    "name": "Ride to the city",
    "description": "go to LA",
    "price": "235",
    "duration": "2 days"
  },
  {
    "tags": [
      { "name": "excursion" },
      { "name": "photos" }
    ],
    "picture": "../img/dog_chill.jpg",
    "name": "Ride to the city",
    "description": "go to LA",
    "price": "235",
    "duration": "2 days"
  },
  {
    "tags": [
      { "name": "excursion" },
      { "name": "photos" }
    ],
    "picture": "../img/photos.jpg",
    "name": "Ride to the city",
    "description": "go to LA",
    "price": "235",
    "duration": "2 days"
  },
  {
    "tags": [
      { "name": "excursion" },
      { "name": "photos" }
    ],
    "picture": "../img/wander_forest.jpg",
    "name": "Ride to the city",
    "description": "go to LA",
    "price": "235",
    "duration": "2 days"
  },
  {
    "tags": [
      { "name": "excursion" },
      { "name": "photos" }
    ],
    "picture": "../img/water_chill.jpg",
    "name": "Ride to the city",
    "description": "go to LA",
    "price": "235",
    "duration": "2 days"
  },
  {
    "tags": [
      { "name": "excursion" },
      { "name": "photos" }
    ],
    "picture": "../img/ride.jpg",
    "name": "Ride to the city",
    "description": "go to LA",
    "price": "235",
    "duration": "2 days"
  },
  {
    "tags": [
      { "name": "excursion" },
      { "name": "photos" }
    ],
    "picture": "../img/beach.jpg",
    "name": "Ride to the city",
    "description": "go to LA",
    "price": "235",
    "duration": "2 days"
  }
]

localStorage.setItem("coupons", JSON.stringify(coupons));
var storedCoupons = JSON.parse(localStorage.getItem("coupons"));
var cards = "";
for (i = 0; i < storedCoupons.length; i++) {
  var coupon = storedCoupons[i];
  var tags = "";
  for (k = 0; k < coupon.tags.length; k++) {
    tags += "<div class=\"tag\">" + coupon.tags[k].name + "</div>";
  }
  var card = 
    "<div class=\"coupon-card\">" +
     tags +
      "<div class=\"coupon-picture\">" +
        "<img class=\"picture\" src=\"" + coupon.picture + "\">" +
      "</div>" +
      "<div class=\"left-coupon-part\">" +
        "<div class=\"font-15 m-top-10 coupon-name\">" + coupon.name + "</div>" +
        "<div class=\"font-10 m-top-10\">" + coupon.description + "</div>" +
        "<div class=\"font-15 m-top-15\">" + coupon.price + "$</div>" +
      "</div>" +
      "<div class=\"right-coupon-part\">" +
        "<div class=\"m-top-10\">" +
          "<svg class=\"f-right\" xmlns=\"http://www.w3.org/2000/svg\" height=\"18\" viewBox=\"0 0 24 24\"" +
            "width=\"18\">" +
            "<path d=\"M0 0h24v24H0z\" fill=\"none\" />" +
            "<path " +
            "d=\"M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z\" />" +
          "</svg>" +
        "</div>" +
        "<div class=\"font-10 m-top-10 light-grey-text m-left-15\">Expires in:" + coupon.duration + "</div>" +
        "<input type=\"submit\" class=\"coupon-button m-top-15 f-right\" value=\"  Add to cart\">" +
      "</div>" +
    "</div>";
  cards += card;
}
console.log(cards)
document.getElementById("cards").innerHTML = cards;


window.onload = function () {
  var divs = document.getElementsByClassName("coupon-cards-loader");
  for (var i = 0; i < divs.length; i++) {
    divs[i].classList.toggle('fade');
  }
  var divs = document.getElementsByClassName("categories-line-loader");
  for (var i = 0; i < divs.length; i++) {
    divs[i].classList.toggle('fade');
  }
}

document.getElementById('name-search').onkeyup = function () {
  var names = document.getElementsByClassName("coupon-name");
  var searhedName = document.getElementById('name-search').value;
  for (index = 0; index < names.length; index++) {
    element = names[index];
    if (!element.textContent.toLowerCase().startsWith(searhedName.toLowerCase())) {
      element.parentElement.parentElement.style.display = "none";
    } else {
      element.parentElement.parentElement.style.display = "block";
    }
  }
}

function searchByTag(id) {
  var tag = document.getElementById(id).getElementsByClassName('tag-search')[0].textContent;
  var coupons = document.getElementsByClassName('coupon-card');
  for (index = 0; index < coupons.length; index++) {
    element = coupons[index];
    tags = element.getElementsByClassName("tag");
    wasFound = false;
    for (i = 0; i < tags.length; i++) {
      currentTag = tags[i].textContent;
      if (new Intl.Collator().compare(currentTag, tag) === 0) {
        wasFound = true;
        element.style.display = "block";
      }
    }
    if (!wasFound) {
      element.style.display = "none";
    }
  }
}
