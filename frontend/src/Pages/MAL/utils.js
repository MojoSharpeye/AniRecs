
// fetches response from backend, pass in the route
export async function fetchProfile(url) {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error('Failed to fetch profile');
    }
    return response.json();
}

//Just a file full of helper functions to use throughout components
//Turns something like finished_airing into Finished Airing
export function formatString(str) {
  return str
    .replace(/_/g, ' ')                      
    .split(' ')                              
    .map(word => word.charAt(0).toUpperCase() + word.slice(1)) 
    .join(' ');                              
}

export function cleanSynopsis(synopsis) {
  const watermark = "[Written by MAL Rewrite]";
  return synopsis.replace(watermark, "").trim();
}