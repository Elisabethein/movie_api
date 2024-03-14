function createSeatingPlan(seatingArray) {
    const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svg.setAttribute('width', '400');
    svg.setAttribute('height', '200');
  
    // Assuming each seat is 20x20 units
    for (let row = 0; row < seatingArray.length; row++) {
      for (let col = 0; col < seatingArray[row].length; col++) {
        const seat = seatingArray[row][col];
        const rect = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
        rect.setAttribute('x', col * 20);
        rect.setAttribute('y', row * 20);
        rect.setAttribute('width', '20');
        rect.setAttribute('height', '20');
        rect.setAttribute('fill', seat === 'x' ? 'red' : 'green');
        svg.appendChild(rect);
      }
    }
  
    // Append the SVG to your HTML container
    document.getElementById('seating-container').appendChild(svg);
  }