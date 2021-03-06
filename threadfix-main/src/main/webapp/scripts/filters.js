var filtersModule = angular.module('threadfixFilters', []);

filtersModule.filter('shortCweNames', function() {
    var akaRegex = /.*\(aka (.*)\)/;
    var parensRegex = /.*\('(.*)'\)/;
    return function(input) {
        var test1 = akaRegex.exec(input);

        if (test1) {
            return test1[1];
        }

        var test2 = parensRegex.exec(input);

        if (test2) {
            return test2[1];
        }

        return input;
    }
});

