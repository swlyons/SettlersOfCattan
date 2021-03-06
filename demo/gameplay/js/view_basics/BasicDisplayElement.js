// STUDENT-CORE-BEGIN
// DO NOT EDIT THIS FILE
var catan = catan || {};
catan.definitions = catan.definitions || {};
catan.definitions.DisplayElement = catan.definitions.DisplayElement || {};

catan.definitions.DisplayElement.getImageSource = function (value) {
    var ResourceImages = catan.definitions.ResourceImages;
    var BuyableImages = catan.definitions.BuyableImages;
    var SpecialBuyables = catan.definitions.SpecialBuyables;
    var CardImages = catan.definitions.CardImages;
    var PointImages = catan.definitions.PointImages;
    var MiscImages = catan.definitions.MiscImages;

    if (ResourceImages[value] != undefined)
        return ResourceImages.prefix + ResourceImages[value];
    if (BuyableImages[value] != undefined)
        return BuyableImages.prefix + BuyableImages[value];
    if (CardImages[value] != undefined)
        return CardImages.prefix + CardImages[value];
    if (PointImages[value] != undefined)
        return PointImages.prefix + PointImages[value];
    if (MiscImages[value] != undefined)
        return MiscImages.prefix + MiscImages[value];

    return MiscImages.prefix + MiscImages.refresh;
};
catan.definitions.DisplayElement.BasicElements = {
    InteractiveImage: (function (value, style, action) {
        var image = document.createElement("input");
        image.setAttribute('type', "image");
        image.setAttribute('src', catan.definitions.DisplayElement.getImageSource(value));
        image.setAttribute('class', style);
        image.onclick = action;

        return image;
    }),
    StaticImage: (function (value, style) {
        var image = document.createElement("img");//not "image": not supported.
        image.setAttribute('src', catan.definitions.DisplayElement.getImageSource(value));
        image.setAttribute('class', style);

        return image;
    }),
    Label: (function (style, content) {
        var label = document.createElement("input");
        label.setAttribute("readonly", "true");
        label.setAttribute("class", catan.definitions.InputLabelStyle + style);
        if (content != undefined)
            label.setAttribute("value", content);

        return label;
    }),
};


