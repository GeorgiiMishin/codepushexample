if ! [ -d ./bundles ]; then
mkdir ./bundles
fi

if ! [-d ./bundles/$1]; then
mkdir ./bundles/$1
fi

mkdir ./bundles/$1/$3
npx react-native bundle --platform $1 --dev false --entry-file index.js --bundle-output ./bundles/$1/$3/$2
